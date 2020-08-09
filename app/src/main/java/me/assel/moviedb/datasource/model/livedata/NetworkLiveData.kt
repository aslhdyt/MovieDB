package me.assel.moviedb.datasource.model.livedata

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import me.assel.moviedb.datasource.model.NetworkState
import me.assel.moviedb.datasource.model.parseNetworkState
import retrofit2.Response
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class NetworkLiveData<T> constructor(coroutineScope: CoroutineContext,
                                     val cancelMessage: String? = null,
                                     val call: (suspend()-> Response<T>)?
)
    : RetryableLiveData<NetworkState<T>>(coroutineScope) {

    constructor(parentScope: CoroutineScope, call: (suspend () -> Response<T>)?)
            : this(parentScope.coroutineContext, null, call)
    constructor(call: suspend() -> Response<T>)
            : this(EmptyCoroutineContext, null, call)
    private constructor(cancelMessage: String? = null)
            : this(EmptyCoroutineContext, cancelMessage, null)
    private constructor()
            : this(EmptyCoroutineContext, null, null)

    companion object {
        const val EMPTY_CALL = "call is empty"

        //to keep object type without any call, result alway NetworkState.Cancelled
        fun <T> empty(reason: String? = null) = NetworkLiveData<T>(reason)

        //get from androidx.lifecycle.Transformations.switchMap
        //modified to return NetworkLiveData
        fun <Y, X> switchMap(source: LiveData<X>, switchMapFunction: Function<X, NetworkLiveData<Y>>): NetworkLiveData<Y> {
            val result = NetworkLiveData<Y>()
            result.addSource(source, object : Observer<X?> {
                var mSource: NetworkLiveData<Y>? = null

                override fun onChanged(x: X?) {
                    if (x == null) return
                    val newLiveData = switchMapFunction.apply(x)
                    if (mSource === newLiveData) {
                        return
                    }
                    if (mSource != null) {
                        result.removeSource(mSource!!)
                    }
                    mSource = newLiveData
                    if (mSource != null) {
                        result.addSource(mSource!!) { y -> result.setValue(y) }
                    }
                }
            })
            return result
        }

        //higher order function shortcut for function above
        inline fun <X, Y> switchMap(
                source: LiveData<X>,
                crossinline transform: (X) -> NetworkLiveData<Y>
        ): NetworkLiveData<Y> = switchMap(source, Function { transform(it) })
    }

    override suspend fun fetchData(isRetry: Boolean) {
        //no network call again if state is success and distinctSuccess is true
        //but when retry is invoke, force to call network
        if (!distinctSuccess || value !is NetworkState.Success || isRetry) {
            postValue(NetworkState.Loading)
            val response = if (call != null) parseNetworkState { call.invoke() } else NetworkState.Cancelled(cancelMessage
                    ?: EMPTY_CALL)
            postValue(response)
            if (response is NetworkState.Success) {
                onSuccess(response.result)
            } else if (response is NetworkState.Failed) {
                onFailed(response)
            }
        }
    }

    private var onSuccess: suspend (T)->Unit = {}
    fun onSuccess(onSuccess: suspend (T)->Unit): NetworkLiveData<T> {
        this.onSuccess = onSuccess
        return this
    }
    private var onFailed: suspend (NetworkState.Failed)->Unit = {}
    fun onFailed(onFailed: suspend (NetworkState.Failed)->Unit) : NetworkLiveData<T> {
        this.onFailed = onFailed
        return this
    }
    fun mapResult(): LiveData<T?> = Transformations.map(this) {
        if (it !is NetworkState.Success) null
        else it.result
    }

    //set true for after success once, no need to call network again
    private var distinctSuccess: Boolean = false
    fun distinctSuccess(b: Boolean = true): NetworkLiveData<T> {
        distinctSuccess = b
        return this
    }
}

//for regular liveData with NetworkState
fun <T> LiveData<NetworkState<T>>.mapResult() = Transformations.map(this) {
    if (it !is NetworkState.Success) null
    else it.result
}
