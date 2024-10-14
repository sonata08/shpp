package com.example.myprofile.data.network.interceptor

import com.example.myprofile.data.datastore.repository.DataStoreRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class TokenInterceptor @Inject constructor(
    private val dataStore: DataStoreRepository
) : Interceptor {

    // Intercepts every request to add the current token
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            dataStore.getTokens().accessToken  // Get the current token
        }
        // If token is available, add it to the request
        val newRequest = if (accessToken.isNotEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(newRequest)
    }
}