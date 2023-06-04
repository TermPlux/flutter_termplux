package io.termplux.flutter_termplux

import android.view.View
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngine

interface TermPlux {

    fun configure(flutterEngine: FlutterEngine)

    fun attach(view: View?, flutterFragment: FlutterFragment): View

    fun clean(flutterEngine: FlutterEngine)

}