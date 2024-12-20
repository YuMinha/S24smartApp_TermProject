package kr.ac.kumoh.ce.s20220703.s24appterm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SongViewModel : ViewModel() {
    private val songApi: SongApi

    // 노래 리스트
    private val _songList = MutableLiveData<List<Song>>()
    val songList: LiveData<List<Song>>
        get() = _songList

    // 앨범 리스트
    private val _albumList = MutableLiveData<List<Album>>()
    val albumList: LiveData<List<Album>>
        get() = _albumList

    // 특정 앨범의 노래 리스트
    private val _songsByAlbum = MutableLiveData<List<Song>>()
    val songsByAlbum: LiveData<List<Song>>
        get() = _songsByAlbum

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(SongApiConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        songApi = retrofit.create(SongApi::class.java)
        fetchSongs()
        fetchAlbums()
    }

    private fun fetchAlbums() {
        viewModelScope.launch {
            try {
                val response = songApi.getAlbums() // 전체 앨범 데이터 가져오기
                _albumList.value = response
            } catch (e: Exception) {
                Log.e("fetchAlbums", e.toString())
            }
        }
    }

    private fun fetchSongs() {
        viewModelScope.launch {
            try {
                val response = songApi.getSongs() // 전체 노래 데이터 가져오기
                _songList.value = response
            } catch (e: Exception) {
                Log.e("fetchSongs", e.toString())
            }
        }
    }

    fun filterSongsByAlbum(albumId: String) {
        val allSongs = _songList.value ?: emptyList()
        _songsByAlbum.value = allSongs.filter { it.albumId == albumId }
    }
}