package io.termplux.flutter_termplux_example

import com.idlefish.flutterboost.FlutterBoost
import io.flutter.app.FlutterApplication
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant

class Application : FlutterApplication() {

    override fun onCreate() {
        super.onCreate()
        FlutterBoost.instance().setup(
            this@Application,
            BoostDelegate {}
        ) { engine: FlutterEngine? ->
            engine?.let {
                GeneratedPluginRegistrant.registerWith(it)
            }
        }
    }
}