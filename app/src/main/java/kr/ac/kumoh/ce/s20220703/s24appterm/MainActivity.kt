package kr.ac.kumoh.ce.s20220703.s24appterm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kr.ac.kumoh.ce.s20220703.s24appterm.ui.theme.S24APPTERMTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            S24APPTERMTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: SongViewModel = viewModel()) {
    val songList by viewModel.songList.observeAsState(emptyList())
    val albumList by viewModel.albumList.observeAsState(emptyList())

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerSheet(drawerState){
                navController.navigate(it){
                    launchSingleTop = true
                    popUpTo(it){ inclusive = true }
                }
            }
        },
        gesturesEnabled = true,
    ){
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {TopBar(drawerState)},
            bottomBar = {
                BottomNavigationBar {
                    navController.navigate(it) {
                        launchSingleTop = true
                        popUpTo(it) { inclusive = true }
                    }
                }
            },
            ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = SongScreen.Song.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = SongScreen.Song.name) {
                    SongList() {
                        navController.navigate(it) {
                            launchSingleTop = true
                            popUpTo(it) { inclusive = true }
                        }
                    }
                }
                composable(route = SongScreen.Album.name) {
                    AlbumList() {
                        navController.navigate(it) {
                            launchSingleTop = true
                            popUpTo(it) { inclusive = true }
                        }
                    }
                }
                composable(
                    route = SongScreen.AlbumDetail.name + "/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id") ?: ""
                    val album = albumList.find { it.id == id }
                    if (album != null) {
                        AlbumDetail(viewModel = viewModel, album = album, onNavigate = { route ->
                            navController.navigate(route)
                        })
                    }
                }


                composable(
                    route = SongScreen.SongDetail.name + "/{title}",
                    arguments = listOf(navArgument("title") {
                        type = NavType.StringType
                    })
                ) {
                    val title = it.arguments?.getString("title") ?: ""
                    val song = songList.find { song -> song.title == title }
                    if (song != null)
                        SongDetail(song)
                }
            }
        }
    }
}

@Composable
fun DrawerSheet(
    drawerState: DrawerState,
    onNavigate: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        NavigationDrawerItem(
            label = { Text("앨범 리스트") },
            selected = false,
            onClick = {
                onNavigate(SongScreen.Album.name)
                scope.launch {
                    drawerState.close()
                }
            },
            icon = {
                Icon(
                    Icons.Filled.Face,
                    contentDescription = "앨범 리스트 아이콘"
                )
            }
        )
        NavigationDrawerItem(
            label = { Text("노래 리스트") },
            selected = false,
            onClick = {
                onNavigate(SongScreen.Song.name)
                scope.launch {
                    drawerState.close()
                }
            },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "노래 리스트 아이콘"
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    CenterAlignedTopAppBar(
        title = { Text("신용재's 노래들") },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "메뉴 아이콘"
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

@Composable
fun BottomNavigationBar(onNavigate: (String) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            label = {
                Text("Albums")
            },
            icon = {
                Icon(
                    Icons.Filled.Face,
                    contentDescription = "앨범 리스트 아이콘"
                )
            },
            selected = false,
            onClick = {
                onNavigate(SongScreen.Album.name)
            }
        )
        NavigationBarItem(
            label = {
                Text("Songs")
            },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "노래 리스트 아이콘"
                )
            },
            selected = false,
            onClick = {
                onNavigate(SongScreen.Song.name)
            }
        )
    }
}


