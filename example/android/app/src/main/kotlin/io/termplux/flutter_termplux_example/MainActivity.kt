package io.termplux.flutter_termplux_example

import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import io.flutter.app.FlutterApplication
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant
import io.termplux.flutter_termplux.FlutterTermPluxActivity
import java.lang.ref.WeakReference

class MainActivity : FlutterTermPluxActivity() {


    override fun onCreateFlutterBoost(application: FlutterApplication) {
        WeakReference(application).get()?.apply {
            FlutterBoost.instance().setup(
                this@apply,
                this@MainActivity,
                this@MainActivity
            )
        }
    }

    override fun onCreateFlutterPlugin(engine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(engine)
    }

    override fun onFlutterRoutePush(options: FlutterBoostRouteOptions) {
        val intent = FlutterBoostActivity.CachedEngineIntentBuilder(
            FlutterActivity::class.java
        )
            .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
            .destroyEngineWithActivity(false)
            .uniqueId(options.uniqueId())
            .url(options.pageName())
            .urlParams(options.arguments())
            .build(FlutterBoost.instance().currentActivity())
        FlutterBoost.instance().currentActivity().startActivity(intent)
    }
}