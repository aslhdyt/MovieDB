package me.assel.moviedb.ui.genre.movie.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import me.assel.moviedb.datasource.model.livedata.NetworkLiveData
import me.assel.moviedb.datasource.network.getNetworkService

class MovieDetailViewModel(movieId: Int, application: Application): AndroidViewModel(application) {
    private val network = getNetworkService()

    val detail = NetworkLiveData(viewModelScope) {
        network.getMovie(movieId)
    }
    val videos = NetworkLiveData(viewModelScope) {
        network.getVideos(movieId)
    }
}
