package me.assel.moviedb.ui.genre.movie

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.assel.moviedb.datasource.model.NetworkState
import me.assel.moviedb.datasource.model.parseNetworkState
import me.assel.moviedb.datasource.network.getNetworkService
import me.assel.moviedb.datasource.network.model.response.DiscoverMovieResponse

class DiscoverMovieDataSource(private val genreId: Int): PageKeyedDataSource<Int, DiscoverMovieResponse.Result>() {

    internal class Factory(private val genreId: Int): DataSource.Factory<Int, DiscoverMovieResponse.Result>() {
        val dataSource = MutableLiveData<DiscoverMovieDataSource>()
        override fun create(): DataSource<Int, DiscoverMovieResponse.Result> {
            val source = DiscoverMovieDataSource(genreId)
            dataSource.postValue(source)
            return source
        }
    }
    val network = getNetworkService()
    val initNetworkState = MutableLiveData<NetworkState<DiscoverMovieResponse>>()
    val loadMoreNetworkState = MutableLiveData<NetworkState<DiscoverMovieResponse>>()
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, DiscoverMovieResponse.Result>) {
        val pageSize = params.requestedLoadSize
        runBlocking {
            initNetworkState.postValue(NetworkState.Loading)
            val response = parseNetworkState { network.discoverMovieByGenre(genreId, 1) }
            delay(5000)
            if (response is NetworkState.Success) {
                val list = response.result.results
                callback.onResult(list, 0, pageSize, 0, 2)
            }
            initNetworkState.postValue(response)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DiscoverMovieResponse.Result>) {
        val page = params.key
        val pageSize = params.requestedLoadSize
        runBlocking {
            loadMoreNetworkState.postValue(NetworkState.Loading)
            val response = parseNetworkState { network.discoverMovieByGenre(genreId, page) }
            delay(5000)
            if (response is NetworkState.Success) {
                callback.onResult(response.result.results, page+1)
            }
            loadMoreNetworkState.postValue(response)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DiscoverMovieResponse.Result>) {
        //ignored since only load to bottom
    }
}