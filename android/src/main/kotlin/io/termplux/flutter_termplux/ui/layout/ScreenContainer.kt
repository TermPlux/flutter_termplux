package io.termplux.flutter_termplux.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.appbar.MaterialToolbar
import io.termplux.flutter_termplux.ui.preview.ScreenPreviews
import io.termplux.flutter_termplux.ui.widget.HostContainer
import io.termplux.flutter_termplux.ui.widget.TopActionBar

@Composable
fun ScreenContainer(
    topBarVisible: Boolean,
    topBarUpdate: (MaterialToolbar) -> Unit,
    hostContainer: FragmentContainerView
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
                    .fillMaxWidth()
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                TopActionBar(
                    modifier = Modifier.fillMaxWidth(),
                    visible = topBarVisible,
                    update = topBarUpdate
                )
            }
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
                HostContainer(
                    hostContainer = hostContainer,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@ScreenPreviews
@Composable
fun ScreenContainerPreview() {

}