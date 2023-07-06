package io.termplux.flutter_termplux

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.calculateDisplayFeatures
import io.termplux.flutter_termplux.ui.layout.ActivityMain
import io.termplux.flutter_termplux.ui.theme.FlutterTermPluxTheme
import kotlinx.coroutines.Runnable
import kotlin.math.hypot

abstract class FlutterTermPluxActivity : FlutterTermPluxPlugin(), Runnable {

    private lateinit var mSplashLogo: AppCompatImageView
    private lateinit var mRootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    override fun Content(
        flutterFrame: FrameLayout,
        navController: NavController,
        configuration: AppBarConfiguration,
        container: FragmentContainerView
    ) {
        super.Content(
            flutterFrame = flutterFrame,
            navController = navController,
            configuration = configuration,
            container = container
        )
//        // 初始化屏闪动画
//        mSplashLogo = AppCompatImageView(this@FlutterTermPluxActivity).apply {
//            setImageDrawable(
//                ContextCompat.getDrawable(
//                    context,
//                    R.drawable.custom_termplux_24
//                )
//            )
//        }
//        val layoutParams = FrameLayout.LayoutParams(
//            FrameLayout.LayoutParams.WRAP_CONTENT,
//            FrameLayout.LayoutParams.WRAP_CONTENT,
//            Gravity.CENTER
//        )
//        addContentView(mSplashLogo, layoutParams)
//        mRootView = LocalView.current
//        mRootView.apply {
//            visibility = View.INVISIBLE
//            post(this@FlutterTermPluxActivity)
//        }
        val windowSize: WindowSizeClass = calculateWindowSizeClass(activity = this)
        val displayFeatures: List<DisplayFeature> = calculateDisplayFeatures(activity = this)
        FlutterTermPluxTheme(
            dynamicColor = true
        ) {
            ActivityMain(
                windowSize = windowSize,
                displayFeatures = displayFeatures,
                rootLayout = flutterFrame,
                appsUpdate = {

                },
                topBarVisible = true,
                topBarUpdate = { toolbar ->
                    setSupportActionBar(toolbar)
                    setupActionBarWithNavController(
                        navController = navController,
                        configuration = configuration
                    )
                },
                fragment = container,
                preferenceUpdate = {

                },
                optionsMenu = {},
                androidVersion = "13",
                shizukuVersion = "13",
                current = {},
                toggle = { /*TODO*/ },
                taskbar = {}
            )
        }
    }

    override fun run() {
        val cx = mSplashLogo.x + mSplashLogo.width / 2f
        val cy = mSplashLogo.y + mSplashLogo.height / 2f
        val startRadius = hypot(
            x = mSplashLogo.width.toFloat(),
            y = mSplashLogo.height.toFloat()
        )
        val endRadius = hypot(
            x = mRootView.width.toFloat(),
            y = mRootView.height.toFloat()
        )
        val circularAnim = ViewAnimationUtils
            .createCircularReveal(
                mRootView,
                cx.toInt(),
                cy.toInt(),
                startRadius,
                endRadius
            )
            .setDuration(
                splashPart2AnimatorMillis.toLong()
            )
        mSplashLogo.animate()
            .alpha(0f)
            .scaleX(1.3f)
            .scaleY(1.3f)
            .setDuration(
                splashPart1AnimatorMillis.toLong()
            )
            .withStartAction {
                mRootView.visibility = View.VISIBLE
                circularAnim.start()
            }
            .withEndAction {
                mSplashLogo.visibility = View.INVISIBLE
            }
            .start()
    }

    companion object {
        /** 开屏图标动画时长 */
        const val splashPart1AnimatorMillis = 600
        const val splashPart2AnimatorMillis = 800
    }
}