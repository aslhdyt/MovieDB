package me.assel.moviedb.datasource.model

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CancellationException
import me.assel.moviedb.R
import me.assel.moviedb.utils.showSnackBar
import me.assel.moviedb.utils.showToast
import okhttp3.Response
import okhttp3.ResponseBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException


//used to define network state
sealed class NetworkState<out T> {
    class Success<T>(val result: T): NetworkState<T>()
    sealed class Failed : NetworkState<Nothing>() {
        class ByException(val t: Throwable) : Failed()
        class ByErrorMessage(val msg: String) : Failed()
        class ByResponse(val response: ResponseBody?, val code : Int = 0): Failed()
        object ByTimeout : Failed()
        object NoConnection: Failed()
    }
    class Cancelled(val reason: String = "no reason") : NetworkState<Nothing>()
    object Loading : NetworkState<Nothing>()
}


//use when(result) to get result
suspend fun <T> parseNetworkState(retrofitCall: (suspend () -> retrofit2.Response<T>)): NetworkState<T> {
    return try {
        val response = retrofitCall.invoke()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkState.Success<T>(body)
        } else {
            if (response.errorBody()?.toString()?.contains("gateway time-out error") == true)
                NetworkState.Failed.ByTimeout
            else {
                val converterError = response.errorBody()
                NetworkState.Failed.ByResponse(converterError, response.code())
            }
        }
    } catch (e: UnknownHostException) {
        NetworkState.Failed.NoConnection
    } catch (e: Throwable) {
        NetworkState.Failed.ByException(e)
    } catch (se3: SocketTimeoutException) {
        NetworkState.Failed.ByTimeout
    }
}


//Extensions error receiver
fun Fragment.handleErrorState(state: NetworkState<*>, retry: (() -> Unit)? = null, onErrorResponse: ((msg: ResponseBody?) -> Unit)? = null) { if (state is NetworkState.Failed) activity?.handleErrorState(state, retry, onErrorResponse) }
fun Context.handleErrorState(state: NetworkState<*>, retry: (() -> Unit)? = null, onErrorResponse: ((msg: ResponseBody?) -> Unit)? = null) { if (state is NetworkState.Failed) handleErrorState(state, retry, onErrorResponse)}
fun Fragment.handleErrorState(state: NetworkState.Failed, retry: (() -> Unit)? = null, onErrorResponse: ((msg: ResponseBody?) -> Unit)? = null) = activity?.handleErrorState(state, retry, onErrorResponse)
fun Context.handleErrorState(state: NetworkState.Failed, retry: (() -> Unit)? = null, onErrorResponse: ((msg: ResponseBody?) -> Unit)? = null) {
    //extension function
    when (state) {
        is NetworkState.Failed.ByException -> {
            val t = state.t
            t.printStackTrace()
            if (t is CancellationException) {
                println("coroutines cancelled") //TODO find couroutine cancellation with message to show
            } else {
                showMessage(t.message ?: getString(R.string.something_went_wrong))
            }
        }
        is NetworkState.Failed.ByErrorMessage -> {
            showToast(state.msg)
        }
        is NetworkState.Failed.ByResponse -> {
            if (onErrorResponse != null) {
                //manually handle error response
                onErrorResponse(state.response)
            } else {
                //if not manually handled, run default function below
                when {
                    state.code > 500 -> showMessage(getString(R.string.something_went_wrong))
                    else -> showMessage("${state.response}")
                }

            }

        }
        NetworkState.Failed.ByTimeout -> {
            showMessage(getString(R.string.timeout_message), indefinite = true, cancelable = true)
            if (retry != null) retry()
        }
        NetworkState.Failed.NoConnection -> {
            showMessage(getString(R.string.no_internet_connection), indefinite = true, cancelable = true)
            if (retry != null) retry()
        }
    }
}
fun Context.showMessage(message: String, indefinite: Boolean = false, cancelable: Boolean = false) {
    if (this is Activity) {
        showSnackBar(message, indefinite, cancelable)
    } else {
        //TODO handle indefinite & cancelable for toast
        showToast(message)
    }
}