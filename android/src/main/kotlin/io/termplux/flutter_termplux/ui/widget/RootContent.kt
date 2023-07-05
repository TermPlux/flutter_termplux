package io.termplux.flutter_termplux.ui.widget

import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import io.termplux.flutter_termplux.R
import io.termplux.flutter_termplux.ui.preview.WidgetPreview

@Composable
fun RootContent(
    rootLayout: FrameLayout,
    modifier: Modifier
) {
    AndroidView(
        factory = {
            return@AndroidView rootLayout
        },
        modifier = modifier
    )
}

@Composable
@WidgetPreview
fun RootContentPreview() {
    val context = LocalContext.current
    RootContent(
        rootLayout = FrameLayout(context).apply {
            addView(
                TextView(context).apply {
                    text = stringResource(
                        id = R.string.flutter_view_preview
                    )
                    gravity = Gravity.CENTER
                },
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    )
}