package me.assel.moviedb.datasource.network.model.response


import com.google.gson.annotations.SerializedName

data class MovieVideoResponse(
    val id: Int, // 12
    val results: List<Result>
) {
    data class Result(
        val id: String, // 533ec651c3a3685448000010
        @SerializedName("iso_3166_1")
        val iso31661: String, // US
        @SerializedName("iso_639_1")
        val iso6391: String, // en
        val key: String, // SPHfeNgogVs
        val name: String, // Finding Nemo 3D Trailer
        val site: String, // YouTube
        val size: Int, // 720
        val type: String // Trailer
    )
}