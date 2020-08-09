package me.assel.moviedb.datasource.network.model.response


data class GenreListResponse(
    val genres: List<Genre> = listOf()
) {
    data class Genre(
        val id: Int, // 28
        val name: String // Action
    )
}