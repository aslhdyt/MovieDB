package me.assel.moviedb.datasource.network.model.response


import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("status_code")
    val statusCode: Int, // 7
    @SerializedName("status_message")
    val statusMessage: String, // Invalid API key: You must be granted a valid key.
    val success: Boolean? // false
)