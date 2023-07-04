package io.termplux.flutter_termplux.ui.layout

import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import io.termplux.flutter_termplux.R
import io.termplux.flutter_termplux.ui.preview.ScreenPreviews
import io.termplux.flutter_termplux.ui.widget.RootContent

@Composable
fun ScreenHome(
    rootLayout: FrameLayout
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        RootContent(
            rootLayout = rootLayout
        )
    }
}

@Composable
@ScreenPreviews
fun ScreenHomePreview() {
    val context = LocalContext.current
    ScreenHome(
        rootLayout = FrameLayout(context).apply {
            addView(
                TextView(context).apply {
                    text = stringResource(
                        id = R.string.flutter_view_preview
                    )
                },
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            )
        }
    )
}