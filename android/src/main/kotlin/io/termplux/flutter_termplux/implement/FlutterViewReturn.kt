package io.termplux.flutter_termplux.implement

import io.flutter.embedding.android.FlutterView

interface FlutterViewReturn {

    fun onFlutterCreated(flutterView: FlutterView?)
    fun onFlutterDestroy(flutterView: FlutterView?)

}