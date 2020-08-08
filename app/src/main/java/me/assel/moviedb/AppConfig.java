package me.assel.moviedb;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by assel on 6/20/17.
 */

public class AppConfig {
    public static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMG_BASE_URL = "https://image.tmdb.org/t/p/w780";
}
