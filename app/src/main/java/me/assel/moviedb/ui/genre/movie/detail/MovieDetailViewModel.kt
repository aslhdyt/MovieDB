package me.assel.moviedb.ui.genre.movie.detail

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import kotlinx.coroutines.runBlocking
import me.assel.moviedb.datasource.model.NetworkState
import me.assel.moviedb.datasource.model.livedata.NetworkLiveData
import me.assel.moviedb.datasource.model.parseNetworkState
import me.assel.moviedb.datasource.network.getNetworkService
import me.assel.moviedb.datasource.network.model.response.MovieReviewResponse

class MovieDetailViewModel(movieId: Int, application: Application): AndroidViewModel(application) {
    private val network = getNetworkService()

    val detail = NetworkLiveData(viewModelScope) {
        network.getMovie(movieId)
    }
    val videos = NetworkLiveData(viewModelScope) {
        network.getVideos(movieId)
    }

    private val reviewFactory = ReviewDataSouce.Factory(movieId)
    val reviews = LivePagedListBuilder(
            reviewFactory,
            PagedList.Config.Builder()
                    .setPrefetchDistance(10)
                    .build()
    ).build()

    val reviewNetworkState = reviewFactory.dataSource.switchMap {
        it.networkState
    }


    private class ReviewDataSouce private constructor(val movieId: Int): PageKeyedDataSource<Int, MovieReviewResponse.Result>() {
        private val network = getNetworkService()
        val networkState = MutableLiveData<NetworkState<MovieReviewResponse>>()

        class Factory(private val movieId: Int): DataSource.Factory<Int, MovieReviewResponse.Result>() {
            val dataSource = MutableLiveData<ReviewDataSouce>()
            override fun create(): DataSource<Int, MovieReviewResponse.Result> {
                val source = ReviewDataSouce(movieId)
                dataSource.postValue(source)
                return source
            }

        }

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MovieReviewResponse.Result>) {
            runBlocking {
                networkState.postValue(NetworkState.Loading)
                val response = parseNetworkState { network.getReviews(movieId, 1) }
                if (response is NetworkState.Success) {
                    val list = response.result.results
                    callback.onResult(list, 0, list.size, 0, 2)
                }
                networkState.postValue(response)
            }
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieReviewResponse.Result>) {
            val page = params.key
            runBlocking {
                networkState.postValue(NetworkState.Loading)
                val response = parseNetworkState { network.getReviews(movieId, page) }
                if (response is NetworkState.Success) {
                    val list = response.result.results
                    callback.onResult(list, page+1)
                }
                networkState.postValue(response)
            }
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieReviewResponse.Result>) {
            //ignored since only load to bottom
        }
    }
}
