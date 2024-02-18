package com.example.myprofile.data.network.interceptor

import com.example.myprofile.data.network.repository.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Authenticator for handling token refresh when a request returns a 401 (Unauthorized) response.
 *
 * This class implements the Authenticator interface and is responsible for refreshing the access token
 * using the provided TokenManager when a request returns a 401 response.
 *
 * @property tokenManager The TokenManager responsible for refreshing tokens.
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager
): Authenticator {

    // Gets called when a request returns 401 (Unauthorized)
    override fun authenticate(route: Route?, response: Response): Request? {
        // Make sure only one coroutine refreshes the token at a time.
        synchronized(this) {
            var newAccessToken: String?
            runBlocking {
                newAccessToken = tokenManager.refreshTokens()
            }
            // if token was updated successfully -> builds new request
            return if (newAccessToken != null) {
                response.request().newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            } else {
                // unable to update token
                null
            }
        }
    }
}