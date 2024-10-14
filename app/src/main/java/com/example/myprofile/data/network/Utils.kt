package com.example.myprofile.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import com.example.myprofile.R
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.utils.extentions.showShortToast
import com.example.myprofile.utils.getMessageFromHttpException
import com.google.android.material.snackbar.Snackbar
import retrofit2.HttpException
import java.net.ConnectException

/**
 * Executes an API call and handles various exceptions that may occur during the process.
 *
 * @param T The expected return type wrapped in a [UiState] object.
 * @param onApiCall A suspend function that performs the API call and returns a [UiState] with the result.
 * @param onConnectException An optional suspend function that will be executed when a [ConnectException] occurs.
 *
 * @return [UiState] containing the result of the API call or an error message depending on the exception caught.
 *
 * @throws HttpException Catches HTTP-related errors and parses the response to return a meaningful error message.
 * @throws ConnectException Handles connection-related errors such as lack of internet access.
 * @throws Exception Handles all other types of exceptions and returns a generic error message.
 */
suspend fun <T> handleApiCall(
    onApiCall: suspend () -> UiState<T>,
    onConnectException: suspend () -> UiState<T> = { UiState.Error(INTERNET_CONNECTION_FAILED) }
): UiState<T> {
    return try {
        onApiCall()
    } catch (e: ConnectException) {
        onConnectException()
    } catch (e: HttpException) {
        val error = e.response()?.errorBody()?.string()
        UiState.Error(getMessageFromHttpException(error))
    } catch (e: Exception) {
        UiState.Error(e.message ?: UNKNOWN_ERROR)
    }
}

/**
 * Checks if the device has an active internet connection.
 *
 * @param context The context used to check the network connectivity.
 * @return `true` if the device is connected to the internet, `false` otherwise.
 */
fun isOnline(context: Context) =
    (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
        getNetworkCapabilities(activeNetwork)?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }

/**
 * Executes the given action only if the device has an active internet connection.
 *
 * @param context The context used to check the network connectivity.
 * @param view The view used to display a Snackbar if there's no internet connection.
 * @param action The action to be executed if the device is online.
 */
fun executeIfOnline(
    context: Context,
    view: View,
    action: () -> Unit,
) {
    if(isOnline(context)) {
        action()
    } else {
        Snackbar.make(view, R.string.internet_connection_failed, Snackbar.LENGTH_LONG).show()
    }
}