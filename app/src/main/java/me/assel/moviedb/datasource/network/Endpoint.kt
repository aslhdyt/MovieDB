package me.assel.moviedb.datasource.network

import me.assel.moviedb.BuildConfig
import me.assel.moviedb.datasource.network.model.response.DiscoverMovieResponse
import me.assel.moviedb.datasource.network.model.response.GenreListResponse
import me.assel.moviedb.datasource.network.model.response.MovieDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by assel on 5/23/17.
 */
interface Endpoint {
    @GET("genre/movie/list")
    suspend fun getGenreList(@Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DB_API_TOKEN): Response<GenreListResponse>


    @GET("discover/movie")
    suspend fun discoverMovieByGenre(
            @Query("with_genres") genreId: Int,
            @Query("page") page: Int,
            @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DB_API_TOKEN
    ): Response<DiscoverMovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DB_API_TOKEN): Response<MovieDetailResponse>
}