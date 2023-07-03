package io.termplux.flutter_termplux

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.material3.ExperimentalMaterial3Api
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
import kotlinx.coroutines.Runnable

open class FlutterTermPluxPlugin : AppCompatActivity(), FlutterBoostDelegate, FlutterBoost.Callback,
    FlutterPlugin, MethodChannel.MethodCallHandler, FlutterViewReturn, FlutterEngineConfigurator,
    DefaultLifecycleObserver, Runnable {

    private lateinit var mChannel: MethodChannel

    private lateinit var mFlutterView: FlutterView
    private lateinit var mSplashLogo: AppCompatImageView

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSettingsView: ViewPager2

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private lateinit var mToolbar: MaterialToolbar

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
        // 继续执行父类代码
        super<AppCompatActivity>.onCreate(savedInstanceState)
        // 启用边到边
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        // 深色模式跟随系统
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        // 设置页面布局边界
        WindowCompat.setDecorFitsSystemWindows(window, false)


        val binding = ContainerBinding.inflate(layoutInflater)
        mFragmentContainerView = binding.navHostFragmentContentMain
        setContentView(mFragmentContainerView)
        mToolbar = MaterialToolbar(this)
        setSupportActionBar(mToolbar)


        val navView = NavigationView(this).apply {
            inflateHeaderView(R.layout.nav_header_main)
            inflateMenu(R.menu.activity_main_drawer)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(
            binding.navHostFragmentContentMain.id
        ) as NavHostFragment

        navController = navHostFragment.navController
        navController.setGraph(R.navigation.mobile_navigation)

        appBarConfiguration = AppBarConfiguration(
            navGraph = navController.graph
        )


        setupActionBarWithNavController(
            navController = navController,
            configuration = appBarConfiguration
        )

        navView.setupWithNavController(
            navController = navController
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun pushNativeRoute(options: FlutterBoostRouteOptions?) {
        when (options?.pageName()) {

        }
    }

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions?) {
        onFlutterPush(options = options)
    }

    override fun onStart(engine: FlutterEngine?) {
        initFlutterPlugin(engine = engine)
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        mChannel = MethodChannel(flutterPluginBinding.binaryMessenger, plugin_channel)
        mChannel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mChannel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method){
            "getPlatformVersion" -> result.success("Android ${Build.VERSION.RELEASE}")
            else -> result.notImplemented()
        }
    }

    override fun onFlutterCreated(flutterView: FlutterView?) {
        // 获取Flutter视图
        mFlutterView = (flutterView ?: errorFlutterViewNull()).apply {}
        // 移除原Flutter视图
        (flutterView.parent as ViewGroup).removeView(flutterView)
        // 添加生命周期观察者
        setContentView(mFlutterView)
    //    lifecycle.addObserver(this@FlutterTermPluxPlugin)
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {

    }

    override fun cleanUpFlutterEngine(flutterEngine: FlutterEngine) {

    }

    override fun onCreate(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onCreate(owner)

//        setContent {
//            FlutterTermPluxTheme(
//                dynamicColor = true
//            ) {
//                Scaffold(
//                    modifier = Modifier.fillMaxSize(),
//                    topBar = {
//                        TopAppBar(
//                            title = {
//                                Text(text = "example")
//                            }
//                        )
//                    }
//                ) { paddingValues ->
//                    Surface(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(paddingValues),
//                        color = MaterialTheme.colorScheme.background
//                    ) {
//                        AndroidView(
//                            factory = {
//                                mFlutterView
//                            },
//                            modifier = Modifier.fillMaxSize()
//                        )
//                    }
//                }
//            }
//        }
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

    }

    private fun errorFlutterViewNull(): Nothing {
        error("Error: FlutterView is null!")
    }


    /**
     * 初始化FlutterBoost
     */
    open fun initFlutterBoost(application: FlutterApplication) = Unit

    /**
     * 初始化Flutter插件
     */
    open fun initFlutterPlugin(engine: FlutterEngine?) = Unit

    /**
     * 打开新的Flutter页面
     */
    open fun onFlutterPush(options: FlutterBoostRouteOptions?) = Unit


    companion object {
        const val plugin_channel: String = "flutter_termplux"
    }
}