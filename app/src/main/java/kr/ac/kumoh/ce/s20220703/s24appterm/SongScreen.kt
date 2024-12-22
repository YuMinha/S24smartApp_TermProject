package kr.ac.kumoh.ce.s20220703.s24appterm

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

enum class SongScreen {
    Album,
    Song,
    SongDetail,
    AlbumDetail
}

@Composable
fun AlbumList(viewModel: SongViewModel = viewModel(),
              onNavigate: (String) -> Unit) {
    val albumList by viewModel.albumList.observeAsState(emptyList())
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(albumList) { album ->
            AlbumItem(album, onNavigate)
        }
    }
}

@Composable
fun AlbumItem(album: Album, onNavigate: (String) -> Unit) {
    Card(
        modifier = Modifier
            .clickable {
                onNavigate(SongScreen.AlbumDetail.name + "/${album.id}") // 수정: String ID 그대로 전달
            },
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(8.dp)
        ) {
            AsyncImage(
                model = album.image,
                contentDescription = "앨범 이미지 ${album.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(percent = 10)),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(album.title, fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun AlbumDetail(viewModel: SongViewModel = viewModel(), album: Album, onNavigate: (String) -> Unit) {
    val context = LocalContext.current

    val songList by viewModel.songList.observeAsState(emptyList())
    println("Album ID: ${album.id}")
    println("Song List: $songList")
    for(song in songList)
        println("Id : ${song.albumId}")

    val filteredSongs = songList.filter { it.albumId == album.id }
    println(filteredSongs)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = album.image,
            contentDescription = "앨범 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(10))
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            album.title,
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(album.link)
            )
            context.startActivity(intent)
        },
            modifier = Modifier
                .padding(16.dp)
                .height(50.dp)
                .width(150.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(100,255,100, 150),
                contentColor = Color(255, 255, 255, 255)
            )
            ) {
            Text("Melon")
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(filteredSongs) { song ->
                SongItem(song, onNavigate)
            }
        }
    }
}




@Composable
fun SongList(viewModel: SongViewModel = viewModel(),
             onNavigate: (String) -> Unit) {
    val songList by viewModel.songList.observeAsState(emptyList())
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(songList) { song ->
            SongItem(song, onNavigate)
        }
    }
}

@Composable
fun SongDetail(song: Song) {

    Column(
        modifier = Modifier
            .background(Color(241, 241, 241, 255))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                song.title,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                lineHeight = 45.sp,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(song.artist, fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
                model = song.image,
                contentDescription = "노래 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(300.dp),
            )

            Spacer(modifier = Modifier.height(32.dp))
        }


        song.lyrics?.let {
            Text(
                text = it.replace("\\n", "\n"),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                lineHeight = 35.sp
            )
        }
    }
}

@Composable
fun SongItem(song: Song,
             onNavigate: (String) -> Unit) {
    //var (expanded, setExpanded) = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .clickable {
                //setExpanded(!expanded)
                if(!song.title.isNullOrEmpty()) {
                    onNavigate(SongScreen.SongDetail.name + "/${song.title}")
                } },
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(8.dp)
        ) {
            AsyncImage(
                model = song.image,
                contentDescription = "노래 이미지 ${song.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(percent = 10)),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                TextTitle(song.title)
                TextSinger(song.artist)
            }
        }
    }
}

@Composable
fun TextTitle(title: String) {
    Text(
        title,
        fontSize = 25.sp,
        lineHeight = 35.sp
    )
}

@Composable
fun TextSinger(singer: String) {
    Text(singer, fontSize = 10.sp)
}