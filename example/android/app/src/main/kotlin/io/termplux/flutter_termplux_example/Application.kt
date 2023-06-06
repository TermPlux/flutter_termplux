package io.termplux.flutter_termplux_example

import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import io.flutter.app.FlutterApplication
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant
import io.termplux.flutter_termplux.FlutterTermPluxPlugin

class Application : FlutterApplication() {

    override fun onCreate() {
        super.onCreate()
        FlutterBoost.instance().setup(
            this@Application,
            object : FlutterBoostDelegate {
                override fun pushNativeRoute(options: FlutterBoostRouteOptions?) {
                    TODO("Not yet implemented")
                }

                override fun pushFlutterRoute(options: FlutterBoostRouteOptions?) {
                    val intent = FlutterBoostActivity.CachedEngineIntentBuilder(
                        FlutterBoostActivity::class.java
                    )
                        .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
                        .destroyEngineWithActivity(false)
                        .uniqueId(options?.uniqueId())
                        .url(options?.pageName())
                        .urlParams(options?.arguments())
                        .build(FlutterBoost.instance().currentActivity())
                    FlutterBoost.instance().currentActivity().startActivity(intent)
                }
            }
        ) { engine: FlutterEngine? ->
            engine?.let {
                GeneratedPluginRegistrant.registerWith(it)
                FlutterTermPluxPlugin().init(this@Application)
            }
        }
    }
}