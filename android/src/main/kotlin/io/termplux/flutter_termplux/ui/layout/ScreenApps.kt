package io.termplux.flutter_termplux.ui.layout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.recyclerview.widget.RecyclerView
import io.termplux.flutter_termplux.ui.preview.ScreenPreviews
import io.termplux.flutter_termplux.ui.widget.AppsGrid

@Composable
fun ScreenApps(
    appsUpdate: (RecyclerView) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AppsGrid(
            modifier = Modifier.fillMaxSize()
        ) { apps ->
            appsUpdate(apps)
        }
    }
}

@ScreenPreviews
@Composable
fun ScreenAppsPreview() {
    ScreenApps {

    }
}