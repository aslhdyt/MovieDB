package me.assel.moviedb.datasource.network

import com.google.gson.Gson
import me.assel.moviedb.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun retrofitBuilder(): Retrofit {
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BODY

    val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
}
private val endpoint by lazy { retrofitBuilder().create(Endpoint::class.java) }
fun getNetworkService(): Endpoint = endpoint