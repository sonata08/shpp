package com.example.myprofile.data.network

import android.util.Log
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.utils.getMessageFromHttpException
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
        Log.e("FAT_handleApi", "ConnectException: ${e.message}")
        onConnectException()
    } catch (e: HttpException) {
        val error = e.response()?.errorBody()?.string()
        UiState.Error(getMessageFromHttpException(error))
    } catch (e: Exception) {
        UiState.Error(e.message ?: UNKNOWN_ERROR)
    }
}