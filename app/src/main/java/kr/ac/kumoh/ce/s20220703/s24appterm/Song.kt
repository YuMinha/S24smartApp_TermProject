package kr.ac.kumoh.ce.s20220703.s24appterm

data class Album(
    val id: String,
    val title: String,
    val link: String?,
    val image: String // 이미지 URL
)

data class Song(
    val albumId: String,
    val title: String,
    val artist: String,
    val lyrics: String?,
    val image: String // 이미지 URL
)
