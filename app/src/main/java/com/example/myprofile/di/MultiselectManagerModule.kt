package com.example.myprofile.di

import com.example.myprofile.data.network.repository.ContactsRepository
import com.example.myprofile.ui.main.contacts.adapter.MultiselectManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MultiselectManagerModule {

    @Provides
    @Singleton
    fun provideMultiselectManager(contactsRepository: ContactsRepository): MultiselectManager {
        return MultiselectManager(contactsRepository)
    }
}