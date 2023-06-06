package io.termplux.flutter_termplux

import android.app.Application
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.termplux.flutter_termplux.ui.theme.TermPluxTheme
import kotlinx.coroutines.Runnable
import java.lang.ref.WeakReference

class FlutterTermPluxPlugin : FlutterPlugin, MethodCallHandler, TermPlux, DefaultLifecycleObserver, Runnable {

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


    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        mChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_termplux")
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
            findFlutterView(view)?.let { flutter ->
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
            mComposeView = ComposeView(mContext)
            // 设置声明周期
            lifecycle.addObserver(this@FlutterTermPluxPlugin)
        }
        return mComposeView
    }

    override fun run() {

    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        mFlutterView.apply {

        }


        mComposeView.apply {
            setContent {
                TermPluxTheme {
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
        super.onStart(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }

    override fun clean(flutterEngine: FlutterEngine) {

    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mChannel.setMethodCallHandler(null)
    }

    private fun findFlutterView(view: View?): FlutterView? {
        if (view is FlutterView) return view
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                findFlutterView(view.getChildAt(i))?.let {
                    return it
                }
            }
        }
        return null
    }

    companion object {

    }
}