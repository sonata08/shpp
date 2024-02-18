package com.example.myprofile.data.network.repository

interface TokenManager {
    suspend fun refreshTokens(): String?
}