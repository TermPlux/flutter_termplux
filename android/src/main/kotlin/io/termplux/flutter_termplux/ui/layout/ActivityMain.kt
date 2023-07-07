package io.termplux.flutter_termplux.ui.layout

import android.view.View
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material.icons.twotone.FlutterDash
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.blankj.utilcode.util.AppUtils
import com.google.android.material.appbar.MaterialToolbar
import io.termplux.flutter_termplux.R
import io.termplux.flutter_termplux.ui.navigation.ItemType
import io.termplux.flutter_termplux.ui.navigation.Screen
import io.termplux.flutter_termplux.ui.navigation.ScreenType
import io.termplux.flutter_termplux.ui.window.ContentType
import io.termplux.flutter_termplux.ui.window.DevicePosture
import io.termplux.flutter_termplux.ui.window.NavigationType
import io.termplux.flutter_termplux.ui.window.isBookPosture
import io.termplux.flutter_termplux.ui.window.isSeparating
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityMain(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    rootLayout: FrameLayout,
    appsUpdate: (RecyclerView) -> Unit,
    topBarVisible: Boolean,
    topBarUpdate: (MaterialToolbar) -> Unit,
    fragment: FragmentContainerView,
    preferenceUpdate: (ViewPager2) -> Unit,
    optionsMenu: (toolbar: MaterialToolbar) -> Unit,
    androidVersion: String,
    shizukuVersion: String,
    current: (item: Int) -> Unit,
    toggle: () -> Unit,
    taskbar: () -> Unit
) {
    val pages = listOf(
        Screen.Flutter,
        Screen.Divider,
        Screen.Overview,
        Screen.Container,
        Screen.Manager,
        Screen.Settings,
        Screen.Preference,
        Screen.About
    )
    val items = listOf(
        Screen.Overview,
        Screen.Container,
        Screen.Flutter,
        Screen.Manager,
        Screen.Settings
    )

    val navController: NavHostController = rememberNavController()

    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val currentDestination = navBackStackEntry?.destination
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val drawerState: DrawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val navigationType: NavigationType
    val contentType: ContentType

    val foldingFeature =
        displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture = when {
        isBookPosture(
            foldFeature = foldingFeature
        ) -> DevicePosture.BookPosture(
            hingePosition = foldingFeature.bounds
        )

        isSeparating(
            foldFeature = foldingFeature
        ) -> DevicePosture.Separating(
            hingePosition = foldingFeature.bounds,
            orientation = foldingFeature.orientation
        )

        else -> DevicePosture.NormalPosture
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = NavigationType.BottomNavigation
            contentType = ContentType.Single
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = NavigationType.NavigationRail
            contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                ContentType.Dual
            } else {
                ContentType.Single
            }
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                NavigationType.NavigationRail
            } else {
                NavigationType.PermanentNavigationDrawer
            }
            contentType = ContentType.Dual
        }

        else -> {
            navigationType = NavigationType.BottomNavigation
            contentType = ContentType.Single
        }
    }

    @Composable
    fun nav() {
        Column {
            // 头布局
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            all = 16.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.lib_description
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    AnimatedVisibility(
                        visible = navigationType != NavigationType.PermanentNavigationDrawer
                    ) {
                        IconButton(
                            onClick = {
                                if (
                                    navigationType != NavigationType.PermanentNavigationDrawer
                                ) {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuOpen,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
            // 导航列表
            Column(
                modifier = Modifier.verticalScroll(
                    state = rememberScrollState()
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(
                    modifier = Modifier.height(
                        height = 12.dp
                    )
                )
                pages.forEach { item ->
                    when (item.item) {
                        ItemType.Default -> NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = item.imageVector,
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(
                                        id = item.title
                                    )
                                )
                            },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true,
                            onClick = {
                                when (item.type) {
                                    ScreenType.Compose -> navController.navigate(
                                        route = item.route
                                    ) {
                                        popUpTo(
                                            id = navController.graph.findStartDestination().id
                                        ) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }.also {
                                        if (
                                            navigationType != NavigationType.PermanentNavigationDrawer
                                        ) {
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        }
                                    }

                                    ScreenType.Divider -> if (
                                        navigationType != NavigationType.PermanentNavigationDrawer
                                    ) {
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.padding(
                                paddingValues = NavigationDrawerItemDefaults.ItemPadding
                            )
                        )

                        ItemType.Divider -> Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    paddingValues = NavigationDrawerItemDefaults.ItemPadding
                                )
                                .padding(
                                    vertical = 5.dp
                                )
                        )
                    }
                }
                Spacer(
                    modifier = Modifier.height(
                        height = 12.dp
                    )
                )
            }
        }
    }

    @Composable
    fun content() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AnimatedVisibility(
                    visible = true
                ) {
                    LargeTopAppBar(
                        title = {
                            Text(
                                text = stringResource(
                                    id = R.string.lib_name
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        navigationIcon = {
                            AnimatedVisibility(
                                visible = navigationType != NavigationType.PermanentNavigationDrawer
                            ) {
                                IconButton(
                                    onClick = {
                                        if (
                                            navigationType != NavigationType.PermanentNavigationDrawer
                                        ) {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    }
                                ) {
                                    Image(
                                        painter = painterResource(
                                            id = R.drawable.custom_termplux_24
                                        ),
                                        contentDescription = null
                                    )
//                                    Icon(
//                                        imageVector = Icons.Filled.Menu,
//                                        contentDescription = null
//                                    )
                                }
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    navController.navigate(
                                        route = Screen.Manager.route
                                    ) {
                                        popUpTo(
                                            id = navController.graph.findStartDestination().id
                                        ) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = null
                                )
                            }
                        },
                        colors = TopAppBarDefaults.largeTopAppBarColors(),
                        scrollBehavior = scrollBehavior
                    )
                }
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = (navigationType == NavigationType.BottomNavigation)
                ) {
                    NavigationBar(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items.forEach { item ->
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy?.any {
                                    it.route == item.route
                                } == true,
                                onClick = {
                                    if (item.type == ScreenType.Compose) navController.navigate(
                                        route = item.route
                                    ) {
                                        popUpTo(
                                            id = navController.graph.findStartDestination().id
                                        ) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.imageVector,
                                        contentDescription = null
                                    )
                                },
                                enabled = true,
                                label = {
                                    Text(
                                        stringResource(
                                            id = item.title
                                        )
                                    )
                                },
                                alwaysShowLabel = false
                            )
                        }
                    }
                }
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarHostState
                )
            },
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        paddingValues = innerPadding
                    )
            ) {
                AnimatedVisibility(
                    visible = navigationType == NavigationType.NavigationRail
                ) {
                    NavigationRail(
                        modifier = Modifier.fillMaxHeight(),
                        header = {
                            FloatingActionButton(
                                onClick = {

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Terminal,
                                    contentDescription = null
                                )
                            }
                        }
                    ) {
                        Column(
                            modifier = Modifier.verticalScroll(
                                state = rememberScrollState()
                            )
                        ) {
                            items.forEach { item ->
                                NavigationRailItem(
                                    selected = currentDestination?.hierarchy?.any {
                                        it.route == item.route
                                    } == true,
                                    onClick = {
                                        if (item.type == ScreenType.Compose) navController.navigate(
                                            route = item.route
                                        ) {
                                            popUpTo(
                                                id = navController.graph.findStartDestination().id
                                            ) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = item.imageVector,
                                            contentDescription = null
                                        )
                                    },
                                    enabled = true,
                                    label = {
                                        Text(
                                            stringResource(
                                                id = item.title
                                            )
                                        )
                                    },
                                    alwaysShowLabel = false
                                )
                            }
                        }
                    }
                }
                NavHost(
                    navController = navController,
                    startDestination = Screen.Overview.route,
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(connection = scrollBehavior.nestedScrollConnection)
                ) {
                    composable(
                        route = Screen.Overview.route
                    ) {
                        ScreenOverview(
                            navController = navController,
                            shizukuVersion = shizukuVersion
                        )
                    }
                    composable(
                        route = Screen.Container.route
                    ) {
                        ScreenContainer(
                            topBarVisible = topBarVisible,
                            topBarUpdate = topBarUpdate,
                            hostContainer = fragment
                        )
                    }
                    composable(
                        route = Screen.Flutter.route
                    ) {
                        ScreenFlutter(
                            rootLayout = rootLayout
                        )
                    }
                    composable(
                        route = Screen.Manager.route
                    ) {
                        ScreenManager(
                            navController = navController,

                            toggle = toggle,
                            current = current,


                            targetAppName = stringResource(id = R.string.lib_name),
                            targetAppPackageName = AppUtils.getAppPackageName(),
                            targetAppDescription = stringResource(id = R.string.lib_description),
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
                    composable(
                        route = Screen.Settings.route
                    ) {
                        ScreenSettings(
                            navController = navController,
                            scope = scope,
                            snackBarHostState = snackBarHostState,
                            current = current,
                            onTaskBarSettings = taskbar,
                            onSystemSettings = {},
                            onDefaultLauncherSettings = {}
                        )
                    }
                    composable(
                        route = Screen.Preference.route
                    ) {
                        ScreenPreference(
                            preferenceUpdate = preferenceUpdate
                        )
                    }
                    composable(
                        route = Screen.About.route
                    ) {
                        ScreenAbout(
                            scope = scope,
                            snackBarHostState = snackBarHostState,
                            onEasterEgg = {},
                            onNotice = {},
                            onSource = {},
                            onDevGitHub = {},
                            onDevTwitter = {},
                            onTeamGitHub = {}
                        )
                    }
                }

            }
        }


    }


    when (navigationType) {
        NavigationType.PermanentNavigationDrawer -> PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet {
                    nav()
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }

        else -> ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    nav()
                }
            },
            modifier = Modifier.fillMaxSize(),
            drawerState = drawerState,
            gesturesEnabled = true
        ) {
            content()
        }
    }
}