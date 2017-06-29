package me.assel.moviedb.api;

import me.assel.moviedb.model.Page;
import me.assel.moviedb.model.Reviews;
import me.assel.moviedb.model.Videos;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by assel on 5/23/17.
 */

public interface RequestInterface {

    @GET("movie/popular")
    Call<Page> getPopular(
            @Query("api_key")String apiKey,
            @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<Page> getTopRated(
            @Query("api_key")String apiKey,
            @Query("page")int page
    );

    @GET("movie/{id}/videos")
    Call<Videos> getVideos(
            @Path("id")long movieId,
            @Query("api_key")String apiKey
    );

    @GET("movie/{id}/reviews")
    Call<Reviews> getReviews(
            @Path("id")long movieId,
            @Query("api_key")String apiKey
    );
}
