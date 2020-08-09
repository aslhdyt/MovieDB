package me.assel.moviedb.ui.genre

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import me.assel.moviedb.datasource.model.livedata.NetworkLiveData
import me.assel.moviedb.datasource.network.getNetworkService

class GenreViewModel(application: Application): AndroidViewModel(application) {
    private val network = getNetworkService()

    val genreList = NetworkLiveData(viewModelScope) {
        network.getGenreList()
    }.distinctSuccess(true)

}