package com.example.myprofile.di

import android.content.Context
import androidx.room.Room
import com.example.myprofile.data.room.DATABASE_NAME
import com.example.myprofile.data.room.ProfileDatabase
import com.example.myprofile.data.room.dao.ContactDao
import com.example.myprofile.data.room.dao.UserDao
import com.example.myprofile.data.room.repository.DatabaseRepository
import com.example.myprofile.data.room.repository.impl.DatabaseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        ProfileDatabase::class.java, DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideContactDao(database: ProfileDatabase) = database.contactsDao()

    @Provides
    fun provideUserDao(database: ProfileDatabase) = database.userDao()

    @Provides
    @Singleton
    fun provideDatabaseRepository(userDao: UserDao, contactDao: ContactDao): DatabaseRepository =
        DatabaseRepositoryImpl(userDao, contactDao)
}