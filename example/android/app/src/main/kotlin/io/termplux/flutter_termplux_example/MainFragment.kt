package io.termplux.flutter_termplux_example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.engine.FlutterEngine
import io.termplux.flutter_termplux.FlutterTermPluxPlugin

class MainFragment : FlutterBoostFragment() {

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        FlutterTermPluxPlugin().configure(flutterEngine = flutterEngine)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FlutterTermPluxPlugin().attach(
            view = super.onCreateView(
                inflater,
                container,
                savedInstanceState
            ),
            flutterFragment = this@MainFragment
        )
    }

    override fun cleanUpFlutterEngine(flutterEngine: FlutterEngine) {
        super.cleanUpFlutterEngine(flutterEngine)
        FlutterTermPluxPlugin().clean(flutterEngine = flutterEngine)
    }
}