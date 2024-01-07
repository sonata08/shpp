package com.example.myprofile.di

import com.example.myprofile.data.network.BASE_URL
import com.example.myprofile.data.network.ContactsApiService
import com.example.myprofile.data.network.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

//    @Provides
//    @Singleton
//    fun provideRetrofit(): UserApiService {
//        val retrofit = Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
////            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
//            .baseUrl(BASE_URL)
//            .build()
//        return retrofit.create(UserApiService::class.java)
//    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApiService(): UserApiService {
        val retrofit = provideRetrofit()
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideContactsApiService(): ContactsApiService {
        val retrofit = provideRetrofit()
        return retrofit.create(ContactsApiService::class.java)
    }

}