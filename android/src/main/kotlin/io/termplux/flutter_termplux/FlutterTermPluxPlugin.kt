package io.termplux.flutter_termplux

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
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

class FlutterTermPluxPlugin : FlutterPlugin, MethodCallHandler, TermPlux, DefaultLifecycleObserver, Runnable {

    private lateinit var mChannel: MethodChannel

    private lateinit var mContext: Context
    private lateinit var mFlutterFragment: FlutterFragment


    private lateinit var mComposeView: ComposeView
    private lateinit var mFlutterView: FlutterView


    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        mChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_termplux")
        mChannel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else {
            result.notImplemented()
        }
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
            mContext = requireActivity()
            // 设置全局活动实例
            mFlutterFragment = this@apply
            // 初始化ComposeView
            mComposeView = ComposeView(mContext)
            // 设置声明周期
            lifecycle.addObserver(this@FlutterTermPluxPlugin)
        }
        return mComposeView
    }

    override fun clean(flutterEngine: FlutterEngine) {

    }

    override fun run() {

    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        mComposeView.apply {
            setContent {
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
                    AndroidView(
                        factory = {
                            mFlutterView
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = paddingValues)
                    )
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

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mChannel.setMethodCallHandler(null)
    }

    companion object {

    }
}