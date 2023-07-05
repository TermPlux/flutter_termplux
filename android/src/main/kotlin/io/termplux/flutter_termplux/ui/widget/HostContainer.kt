package io.termplux.flutter_termplux.ui.widget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import io.termplux.flutter_termplux.ui.preview.WidgetPreview

@Composable
fun HostContainer(
    hostContainer: FragmentContainerView,
    modifier: Modifier
) {
    AndroidView(
        factory = {
            return@AndroidView hostContainer
        },
        modifier = modifier
    )
}

@Composable
@WidgetPreview
fun HostContainerPreview() {
    HostContainer(
        hostContainer = FragmentContainerView(
            context = LocalContext.current
        ),
        modifier = Modifier.fillMaxSize()
    )
}