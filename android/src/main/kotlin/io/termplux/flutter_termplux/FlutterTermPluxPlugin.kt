package io.termplux.flutter_termplux

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import io.flutter.app.FlutterApplication
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlinx.coroutines.Runnable
import java.lang.ref.WeakReference

open class FlutterTermPluxPlugin : AppCompatActivity(),FlutterBoostDelegate,FlutterBoost.Callback, FlutterPlugin, MethodCallHandler, TermPlux,
    DefaultLifecycleObserver, Runnable {

    private lateinit var mChannel: MethodChannel

    private lateinit var mContext: Context
    private lateinit var mActivity: AppCompatActivity
    private lateinit var mFragment: FlutterFragment

    private lateinit var mComposeView: ComposeView
    private lateinit var mViewPager2: ViewPager2
    private lateinit var mFlutterView: FlutterView
    private lateinit var mSplashLogo: AppCompatImageView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSettingsView: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        if (application is FlutterApplication){
            initFlutterBoost(
                application = (application as FlutterApplication)
            )
        }
        super<AppCompatActivity>.onCreate(savedInstanceState)




        val intent = Intent(this@FlutterTermPluxPlugin, this.javaClass)
    }


    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        mChannel = MethodChannel(flutterPluginBinding.binaryMessenger, plugin_channel)
        mChannel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${Build.VERSION.RELEASE}")
        } else {
            result.notImplemented()
        }
    }

    override fun init(application: Application) {

    }

    override fun configure(flutterEngine: FlutterEngine) {

    }

    override fun attach(view: View?, flutterFragment: FlutterFragment): View {
        WeakReference(flutterFragment).get()?.apply {
            // 获取FlutterView
            findFlutterView(view = view)?.let { flutter ->
                mFlutterView = flutter
            }
            // 移除FlutterView
            (mFlutterView.parent as ViewGroup).removeView(mFlutterView)
            // 设置全局上下文
            mContext = context
            mActivity = activity as AppCompatActivity
            // 设置全局活动实例
            mFragment = this@apply
            // 初始化ComposeView
            mComposeView = ComposeView(context = mContext)
            // 设置声明周期
            lifecycle.addObserver(this@FlutterTermPluxPlugin)
        }
        // 返回View
        return mComposeView
    }

    override fun run() {

    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onCreate(owner)

        mFlutterView.apply {

        }

        mComposeView.apply {
            setContent {
                TermPluxTheme(
                    dynamicColor = true && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
                    activity = mActivity
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(text = "example")
                                }
                            )
                        }
                    ) { paddingValues ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AndroidView(
                                factory = {
                                    mFlutterView
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
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

    override fun clean(flutterEngine: FlutterEngine) {

    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mChannel.setMethodCallHandler(null)

    }

    open fun initFlutterBoost(application: FlutterApplication) {

    }

    open fun initFlutterPlugin(engine: FlutterEngine?){

    }

    private fun findFlutterView(view: View?): FlutterView? {
        when {
            (view is FlutterView) -> return view
            (view is ViewGroup) -> for (i in 0 until view.childCount) {
                findFlutterView(view.getChildAt(i))?.let {
                    return it
                }
            }
        }
        return null
    }

    companion object {


        const val plugin_channel: String = "flutter_termplux"
        const val termplux_channel: String = "termplux_channel"

        private val Purple80: Color = Color(0xFFD0BCFF)
        private val PurpleGrey80: Color = Color(0xFFCCC2DC)
        private val Pink80: Color = Color(0xFFEFB8C8)

        private val Purple40: Color = Color(0xFF6650a4)
        private val PurpleGrey40: Color = Color(0xFF625b71)
        private val Pink40: Color = Color(0xFF7D5260)

        private val Typography: Typography = Typography(
            bodyLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            )
        )

        private val DarkColorScheme = darkColorScheme(
            primary = Purple80,
            secondary = PurpleGrey80,
            tertiary = Pink80
        )

        private val LightColorScheme = lightColorScheme(
            primary = Purple40,
            secondary = PurpleGrey40,
            tertiary = Pink40
        )

        @Composable
        private fun TermPluxTheme(
            dynamicColor: Boolean,
            activity: AppCompatActivity,
            content: @Composable () -> Unit
        ) {
            val darkTheme: Boolean = isSystemInDarkTheme()
            val view: View = LocalView.current
            val window: Window = activity.window
            val systemUiController: SystemUiController = rememberSystemUiController()
            val navController: NavHostController = rememberNavController()

            val colorScheme = when {
                dynamicColor -> {
                    val context = LocalContext.current
                    if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                        context
                    )
                }

                darkTheme -> DarkColorScheme
                else -> LightColorScheme
            }

            if (!view.isInEditMode) {
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !darkTheme
                    )
                    WindowCompat.getInsetsController(
                        window,
                        view
                    ).isAppearanceLightStatusBars = !darkTheme
                }
            }

            MaterialTheme(
                colorScheme = colorScheme,
                typography = Typography,
                content = content
            )
        }
    }



    override fun pushNativeRoute(options: FlutterBoostRouteOptions?) {
        TODO("Not yet implemented")
    }

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions?) {
        TODO("Not yet implemented")
    }

    override fun onStart(engine: FlutterEngine?) {
        initFlutterPlugin(engine = engine)
    }
}