package io.termplux.flutter_termplux

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.internal.EdgeToEdgeUtils
import com.google.android.material.navigation.NavigationView
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import io.flutter.app.FlutterApplication
import io.flutter.embedding.android.FlutterEngineConfigurator
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.termplux.flutter_termplux.databinding.ContainerBinding
import io.termplux.flutter_termplux.implement.FlutterViewReturn
import io.termplux.flutter_termplux.ui.layout.ActivityMain
import io.termplux.flutter_termplux.ui.theme.FlutterTermPluxTheme
import kotlinx.coroutines.Runnable
import kotlin.math.hypot

open class FlutterTermPluxPlugin : AppCompatActivity(), FlutterBoostDelegate, FlutterBoost.Callback,
    FlutterPlugin, MethodChannel.MethodCallHandler, FlutterViewReturn, FlutterEngineConfigurator,
    DefaultLifecycleObserver, Runnable {

    private lateinit var mChannel: MethodChannel

    private lateinit var mFlutterFrameLayout: FrameLayout
    private lateinit var mFlutterTextView: AppCompatTextView
    private lateinit var mSplashLogo: AppCompatImageView
    private lateinit var mRootLayout: FrameLayout

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSettingsView: ViewPager2

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private lateinit var mFragmentContainerView: FragmentContainerView


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        // 初始化FlutterBoost
        when (val application = application) {
            is FlutterApplication -> initFlutterBoost(
                application = application
            )
        }
        // 设置主题
        setTheme(R.style.Theme_FlutterTermPlux)
        super<AppCompatActivity>.onCreate(savedInstanceState)
        // 启用边到边
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        // 深色模式跟随系统
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        // 设置页面布局边界
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // 布局绑定
        val binding = ContainerBinding.inflate(layoutInflater)
        // 获取Fragment容器视图
        mFragmentContainerView = binding.navHostFragmentContentMain
        // 导航主机
        val navHostFragment = supportFragmentManager.findFragmentById(
            mFragmentContainerView.id
        ) as NavHostFragment
        // 获取导航控制器
        navController = navHostFragment.navController
        // 设置导航图
        navController.setGraph(
            graphResId = R.navigation.mobile_navigation,
            startDestinationArgs = Bundle()
        )
        // 初始化操作栏配置器
        appBarConfiguration = AppBarConfiguration(
            navGraph = navController.graph
        )


        mFlutterFrameLayout = FrameLayout(this).apply {
            visibility = View.INVISIBLE
            post(this@FlutterTermPluxPlugin)
        }

        mFlutterTextView = AppCompatTextView(this).apply {
            text = "Flutter视图已销毁\n请返回主页面后继续"
            gravity = Gravity.CENTER
        }

        // 初始化屏闪动画
        mSplashLogo = AppCompatImageView(this@FlutterTermPluxPlugin).apply {
            setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.custom_termplux_24
                )
            )
        }

        // 初始化跟布局
        mRootLayout = FrameLayout(this@FlutterTermPluxPlugin).apply {
            addView(
                mFlutterFrameLayout,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            addView(
                mSplashLogo,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            )
        }

        // 添加生命周期观察者
        lifecycle.addObserver(this@FlutterTermPluxPlugin)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> navController.navigate(R.id.nav_settings)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun pushNativeRoute(options: FlutterBoostRouteOptions?) {
        when (options?.pageName()) {
            "flutter" -> navController.navigate(R.id.nav_flutter)
            "settings" -> navController.navigate(R.id.nav_settings)
        }
    }

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions?) {
        options?.let {
            onFlutterPush(options = it)
        }
    }

    override fun onStart(engine: FlutterEngine?) {
        engine?.let {
            initFlutterPlugin(engine = it)
        }
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        mChannel = MethodChannel(flutterPluginBinding.binaryMessenger, plugin_channel)
        mChannel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mChannel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getPlatformVersion" -> result.success("Android ${Build.VERSION.RELEASE}")
            else -> result.notImplemented()
        }
    }

    override fun onFlutterCreated(flutterView: FlutterView?) {
        (flutterView ?: errorFlutterViewNull()).let { create ->
            (create.parent as ViewGroup).removeView(create)
            if (create.parent != mFlutterFrameLayout) {
                mFlutterFrameLayout.addView(
                    create,
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                )
                if (mFlutterTextView.parent == mFlutterFrameLayout) {
                    mFlutterFrameLayout.removeView(mFlutterTextView)
                }
            }
        }
    }

    override fun onFlutterDestroy(flutterView: FlutterView?) {
        (flutterView ?: errorFlutterViewNull()).let { destroy ->
            if (destroy.parent == mFlutterFrameLayout) {
                mFlutterFrameLayout.removeView(destroy)
                if (mFlutterTextView.parent != mFlutterFrameLayout) {
                    mFlutterFrameLayout.addView(
                        mFlutterTextView,
                        FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER
                        )
                    )
                }

            }
        }
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {

    }

    override fun cleanUpFlutterEngine(flutterEngine: FlutterEngine) {

    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onCreate(owner)
        setContent {
            //Content()
            val windowSize: WindowSizeClass = calculateWindowSizeClass(activity = this)
            val displayFeatures: List<DisplayFeature> = calculateDisplayFeatures(activity = this)
            FlutterTermPluxTheme(
                dynamicColor = true
            ) {
                ActivityMain(
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    rootLayout = mRootLayout,
                    appsUpdate = {

                    },
                    topBarVisible = true,
                    topBarUpdate = { toolbar ->
                        setSupportActionBar(toolbar)
                        setupActionBarWithNavController(
                            navController = navController,
                            configuration = appBarConfiguration
                        )
                    },
                    fragment = mFragmentContainerView,
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
    }

    override fun onStart(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStart(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onDestroy(owner)
    }


    override fun run() {
        val cx = mSplashLogo.x + mSplashLogo.width / 2f
        val cy = mSplashLogo.y + mSplashLogo.height / 2f
        val startRadius = hypot(
            x = mSplashLogo.width.toFloat(),
            y = mSplashLogo.height.toFloat()
        )
        val endRadius = hypot(
            x = mFlutterFrameLayout.width.toFloat(),
            y = mFlutterFrameLayout.height.toFloat()
        )
        val circularAnim = ViewAnimationUtils
            .createCircularReveal(
                mFlutterFrameLayout,
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
            .withEndAction {
                mSplashLogo.visibility = View.GONE
            }
            .withStartAction {
                mFlutterFrameLayout.visibility = View.VISIBLE
                circularAnim.start()
            }
            .start()
    }

    private fun errorFlutterViewNull(): Nothing {
        error("Error: FlutterView is null!")
    }


    open fun initFlutterBoost(application: FlutterApplication) = Unit
    open fun initFlutterPlugin(engine: FlutterEngine) = Unit
    open fun onFlutterPush(options: FlutterBoostRouteOptions) = Unit

    @Composable
    open fun Content() = Unit


    companion object {
        const val plugin_channel: String = "flutter_termplux"

        /** 开屏图标动画时长 */
        const val splashPart1AnimatorMillis = 600
        const val splashPart2AnimatorMillis = 800
    }
}