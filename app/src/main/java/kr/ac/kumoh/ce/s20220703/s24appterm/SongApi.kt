package kr.ac.kumoh.ce.s20220703.s24appterm

import retrofit2.http.GET
import retrofit2.http.Query

interface SongApi {
    @GET("songs")
    suspend fun getSongs(
        @Query("apikey") apiKey: String = SongApiConfig.API_KEY
    ): List<Song>

    @GET("albums")
    suspend fun getAlbums(
        @Query("apikey") apiKey: String = SongApiConfig.API_KEY
    ): List<Album>

    @GET("songs")
    suspend fun getSongsByAlbum(
        @Query("album_id") albumId: String,
        @Query("apikey") apiKey: String = SongApiConfig.API_KEY
    ): List<Song>
}