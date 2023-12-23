package com.example.myprofile.di

import com.example.myprofile.data.repository.AuthRepository
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.data.repository.DataStoreRepository
import com.example.myprofile.data.repository.impl.AuthRepositoryImpl
import com.example.myprofile.data.repository.impl.ContactsRepositoryImpl
import com.example.myprofile.data.repository.impl.DataStoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * This module is responsible for binding the concrete implementation
 * [ContactsRepositoryImpl] to the [ContactsRepository] interface
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindContactsRepository(
        repositoryImpl: ContactsRepositoryImpl
    ): ContactsRepository

    @Binds
    abstract fun bindAuthRepository(
        repositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindDataStoreRepository(
        repositoryImpl: DataStoreRepositoryImpl
    ): DataStoreRepository
}