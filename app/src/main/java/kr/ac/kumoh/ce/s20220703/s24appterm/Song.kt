package kr.ac.kumoh.ce.s20220703.s24appterm

import com.google.gson.annotations.SerializedName

data class Album(
    val id: String,
    val title: String,
    val link: String?,
    val image: String // 이미지 URL
)

data class Song(
    @SerializedName("album_id") val albumId: String,
    val title: String,
    val artist: String,
    val lyrics: String?,
    val image: String // 이미지 URL
)
