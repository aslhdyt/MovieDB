package me.assel.moviedb.datasource.network.model.response


import com.google.gson.annotations.SerializedName

data class GenreListResponse(
    val genres: List<Genre> = listOf()
) {
    data class Genre(
        val id: Int, // 28
        val name: String // Action
    )
}