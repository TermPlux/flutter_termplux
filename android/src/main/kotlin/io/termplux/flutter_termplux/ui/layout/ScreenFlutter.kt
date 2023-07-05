package io.termplux.flutter_termplux.ui.layout

import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.termplux.flutter_termplux.R
import io.termplux.flutter_termplux.ui.preview.ScreenPreviews
import io.termplux.flutter_termplux.ui.theme.FlutterTermPluxTheme
import io.termplux.flutter_termplux.ui.widget.RootContent

@Composable
fun ScreenFlutter(
    rootLayout: FrameLayout
) {
    val scrollState = rememberScrollState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    state = scrollState
                )
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(
                        weight = 1f,
                        fill = true
                    )
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    )
            ) {
                RootContent(
                    rootLayout = rootLayout,
                    modifier = Modifier
                        .fillMaxSize()
                        .height(
                            intrinsicSize = IntrinsicSize.Max
                        )
                )
            }
        }
    }
}

@Composable
@ScreenPreviews
fun ScreenFlutterPreview() {
    val context = LocalContext.current
    FlutterTermPluxTheme {
        ScreenFlutter(
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
}