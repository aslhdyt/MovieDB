package me.assel.moviedb.datasource.network.model.response


import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    val adult: Boolean, // false
    @SerializedName("backdrop_path")
    val backdropPath: String, // /dFYguAfeVt19qAbzJ5mArn7DEJw.jpg
    @SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection,
    val budget: Int, // 94000000
    val genres: List<Genre>,
    val homepage: String, // http://movies.disney.com/finding-nemo
    val id: Int, // 12
    @SerializedName("imdb_id")
    val imdbId: String, // tt0266543
    @SerializedName("original_language")
    val originalLanguage: String, // en
    @SerializedName("original_title")
    val originalTitle: String, // Finding Nemo
    val overview: String, // Nemo, an adventurous young clownfish, is unexpectedly taken from his Great Barrier Reef home to a dentist's office aquarium. It's up to his worrisome father Marlin and a friendly but forgetful fish Dory to bring Nemo home -- meeting vegetarian sharks, surfer dude turtles, hypnotic jellyfish, hungry seagulls, and more along the way.
    val popularity: Double, // 30.549
    @SerializedName("poster_path")
    val posterPath: String, // /xVNSgrsvpcAHPnyKf2phYxyppNZ.jpg
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>,
    @SerializedName("release_date")
    val releaseDate: String, // 2003-05-30
    val revenue: Long, // 940335536
    val runtime: Int, // 100
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    val status: String, // Released
    val tagline: String, // There are 3.7 trillion fish in the ocean. They're looking for one.
    val title: String, // Finding Nemo
    val video: Boolean, // false
    @SerializedName("vote_average")
    val voteAverage: Double, // 7.8
    @SerializedName("vote_count")
    val voteCount: Int // 13857
) {
    data class BelongsToCollection(
        @SerializedName("backdrop_path")
        val backdropPath: String, // /2hC8HHRUvwRljYKIcQDMyMbLlxz.jpg
        val id: Int, // 137697
        val name: String, // Finding Nemo Collection
        @SerializedName("poster_path")
        val posterPath: String // /xwggrEugjcJDuabIWvK2CpmK91z.jpg
    )

    data class Genre(
        val id: Int, // 16
        val name: String // Animation
    )

    data class ProductionCompany(
        val id: Int, // 3
        @SerializedName("logo_path")
        val logoPath: String, // /1TjvGVDMYsj6JBxOAkUHpPEwLf7.png
        val name: String, // Pixar
        @SerializedName("origin_country")
        val originCountry: String // US
    )

    data class ProductionCountry(
        @SerializedName("iso_3166_1")
        val iso31661: String, // US
        val name: String // United States of America
    )

    data class SpokenLanguage(
        @SerializedName("iso_639_1")
        val iso6391: String, // en
        val name: String // English
    )
}