package me.assel.moviedb.datasource.network.model.response


import com.google.gson.annotations.SerializedName

data class DiscoverMovieResponse(
    val page: Int, // 1
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int, // 4
    @SerializedName("total_results")
    val totalResults: Int // 61
) {
    data class Result(
        val adult: Boolean, // false
        @SerializedName("backdrop_path")
        val backdropPath: Any?, // null
        @SerializedName("genre_ids")
        val genreIds: List<Int>,
        val id: Int, // 164558
        @SerializedName("original_language")
        val originalLanguage: String, // en
        @SerializedName("original_title")
        val originalTitle: String, // One Direction: This Is Us
        val overview: String, // Go behind the scenes during One Directions sell out "Take Me Home" tour and experience life on the road.
        val popularity: Double, // 1.166982
        @SerializedName("poster_path")
        val posterPath: String?, // null
        @SerializedName("release_date")
        val releaseDate: String, // 2013-08-30
        val title: String, // One Direction: This Is Us
        val video: Boolean, // false
        @SerializedName("vote_average")
        val voteAverage: Double, // 8.45
        @SerializedName("vote_count")
        val voteCount: Int // 55
    )
}