package com.example.myprofile.di

import com.example.myprofile.data.model.UserInfoHolder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HoldersModule {

    @Provides
    @Singleton
    fun provideUserInfoHolder(): UserInfoHolder = UserInfoHolder
}