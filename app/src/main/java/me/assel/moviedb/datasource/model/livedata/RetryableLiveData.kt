package me.assel.moviedb.datasource.model.livedata

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


abstract class RetryableLiveData<T>(private val parentContext: CoroutineContext)
    : MediatorLiveData<T>(), CoroutineScope by CoroutineScope(parentContext + Dispatchers.IO) {

    abstract suspend fun fetchData(isRetry: Boolean)
    private var job: Job? = null
    fun retry() {
        job?.cancel()
        job = launch { fetchData(true) }
    }
    override fun onActive() {
        super.onActive()
        job = launch { fetchData(false) }
    }
    override fun onInactive() {
        super.onInactive()
        job?.cancel()
    }
}