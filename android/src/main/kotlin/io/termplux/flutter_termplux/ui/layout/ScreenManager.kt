package io.termplux.flutter_termplux.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.blankj.utilcode.util.AppUtils
import com.google.android.material.appbar.MaterialToolbar
import io.termplux.flutter_termplux.R
import io.termplux.flutter_termplux.ui.preview.ScreenPreviews
import io.termplux.flutter_termplux.ui.widget.TopActionBar

@Composable
fun ScreenManager(
    navController: NavHostController,
    toggle: () -> Unit,
    current: (item: Int) -> Unit,
    topBarVisible: Boolean,
    topBarUpdate: (MaterialToolbar) -> Unit,
    fragment: FragmentContainerView,
    targetAppName: String,
    targetAppPackageName: String,
    targetAppDescription: String,
    targetAppVersionName: String,
    NavigationOnClick: () -> Unit,
    MenuOnClick: () -> Unit,
    SearchOnClick: () -> Unit,
    SheetOnClick: () -> Unit,
    AppsOnClick: () -> Unit,
    SelectOnClick: () -> Unit,
    onNavigateToApps: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val expandedPowerButton = remember {
        mutableStateOf(
            value = true
        )
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
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
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 5.dp,
                    bottom = 8.dp
                )
            ) {
                TopActionBar(
                    modifier = Modifier.fillMaxWidth(),
                    visible = topBarVisible,
                    update = topBarUpdate
                )
            }
            ElevatedCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FilledIconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = null
                            )
                        }
                        Spacer(
                            modifier = Modifier.size(
                                size = ButtonDefaults.IconSpacing
                            )
                        )
                        FilledIconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = null
                            )
                        }
                        Spacer(
                            modifier = Modifier.size(
                                size = ButtonDefaults.IconSpacing
                            )
                        )
                        FilledIconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Apps,
                                contentDescription = null
                            )
                        }
                        Spacer(
                            modifier = Modifier.size(
                                size = ButtonDefaults.IconSpacing
                            )
                        )
                        FilledIconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        }
                        Spacer(
                            modifier = Modifier.size(
                                size = ButtonDefaults.IconSpacing
                            )
                        )
                        FilledIconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SelectAll,
                                contentDescription = null
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .clickable {

                        }
                ) {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Search...",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
            ElevatedCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            all = 5.dp
                        )
                        .heightIn(min = 70.dp)
                ) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.custom_termplux_24
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(
                                width = 48.dp,
                                height = 48.dp
                            )
                            .padding(
                                top = 5.dp
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 1f)
                    ) {
                        Text(
                            text = targetAppName,
                            modifier = Modifier.padding(start = 5.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = targetAppPackageName,
                            modifier = Modifier.padding(start = 5.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = targetAppDescription,
                            modifier = Modifier.padding(start = 5.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = targetAppVersionName,
                        modifier = Modifier
                            .padding(all = 5.dp)
                            .wrapContentWidth(Alignment.End)
                    )
                    Switch(
                        checked = expandedPowerButton.value,
                        onCheckedChange = {
                            expandedPowerButton.value = it
                        },
                        modifier = Modifier
                            .padding(all = 5.dp)
                            .wrapContentWidth(Alignment.End),
                        enabled = true
                    )
                }
            }
            ElevatedCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            all = 5.dp
                        )
                ) {
                    Text(text = "运行环境电源")
                    AssistChip(
                        onClick = {

                        },
                        label = {
                            Text(
                                text = "启动运行环境",
                                color = Color.Green
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = expandedPowerButton.value,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ToggleOn,
                                contentDescription = null,
                                modifier = Modifier.size(AssistChipDefaults.IconSize),
                                tint = Color.Green
                            )
                        }
                    )
                    AssistChip(
                        onClick = {

                        },
                        label = {
                            Text(
                                text = "停止运行环境",
                                color = Color.Red
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = expandedPowerButton.value,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ToggleOff,
                                contentDescription = null,
                                modifier = Modifier.size(AssistChipDefaults.IconSize),
                                tint = Color.Red
                            )
                        }
                    )
                }
            }
            ElevatedCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 5.dp
                )
            ) {
                AndroidView(
                    factory = {
                        return@AndroidView fragment
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
@ScreenPreviews
private fun ScreenManagerPreview() {
    ScreenManager(
        navController = rememberNavController(),
        toggle = {},
        current = {},
        topBarVisible = true,
        topBarUpdate = {},
        fragment = FragmentContainerView(context = LocalContext.current),
        targetAppName = stringResource(
            id = R.string.lib_name
        ),
        targetAppPackageName = AppUtils.getAppPackageName(),
        targetAppDescription = stringResource(
            id = R.string.lib_description
        ),
        targetAppVersionName = AppUtils.getAppVersionName(),
        NavigationOnClick = {},
        MenuOnClick = {},
        SearchOnClick = {},
        SheetOnClick = {},
        AppsOnClick = {},
        SelectOnClick = {},
        onNavigateToApps = {}
    )
}