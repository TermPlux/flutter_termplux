package io.termplux.flutter_termplux

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

abstract class FlutterTermPluxActivity : FlutterTermPluxPlugin() {

    @Composable
    override fun Content() {
        super.Content()
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
            ){

            }
        }
    }
}