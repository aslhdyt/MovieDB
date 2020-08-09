package me.assel.moviedb.datasource.network.model.response


import com.google.gson.annotations.SerializedName

data class MovieReviewResponse(
    val id: Int, // 12
    val page: Int, // 1
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int, // 1
    @SerializedName("total_results")
    val totalResults: Int // 2
) {
    data class Result(
        val author: String, // Dave09
        val content: String, // One of the best animated films I have ever seen. Great characters, amusing animation, and laugh-out-loud humor. Also, watch for the little skit shown after the credits. It's all great stuff that simply must be seen.
        val id: String, // 52d4a742760ee30e2d0dac9d
        val url: String // https://www.themoviedb.org/review/52d4a742760ee30e2d0dac9d
    )
}