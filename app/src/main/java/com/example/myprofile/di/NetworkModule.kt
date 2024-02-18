package com.example.myprofile.di


import com.example.myprofile.BuildConfig
import com.example.myprofile.data.network.api.ContactsApiService
import com.example.myprofile.data.network.api.UserApiService
import com.example.myprofile.data.network.interceptor.TokenAuthenticator
import com.example.myprofile.data.network.repository.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTokenAuthenticator(tokenManager: TokenManager): Authenticator {
        return TokenAuthenticator(tokenManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenAuthenticator: TokenAuthenticator): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideContactsApiService(retrofit: Retrofit): ContactsApiService {
        return retrofit.create(ContactsApiService::class.java)
    }

}