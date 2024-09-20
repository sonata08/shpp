package com.example.myprofile.di

import com.example.myprofile.data.network.repository.AuthRepository
import com.example.myprofile.data.network.repository.ContactsRepository
import com.example.myprofile.data.network.repository.TokenManager
import com.example.myprofile.data.datastore.repository.DataStoreRepository
import com.example.myprofile.data.network.repository.impl.AuthRepositoryImpl
import com.example.myprofile.data.network.repository.impl.ContactsRepositoryImpl
import com.example.myprofile.data.network.repository.impl.TokenManagerImpl
import com.example.myprofile.data.datastore.repository.impl.DataStoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindContactsRepository(
        repositoryImpl: ContactsRepositoryImpl
    ): ContactsRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        repositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindDataStoreRepository(
        repositoryImpl: DataStoreRepositoryImpl
    ): DataStoreRepository

    @Binds
    @Singleton
    abstract fun bindTokenManager(
        tokenManagerImpl: TokenManagerImpl
    ): TokenManager
}