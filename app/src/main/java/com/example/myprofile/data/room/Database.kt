package com.example.myprofile.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myprofile.data.room.dao.ContactDao
import com.example.myprofile.data.room.dao.UserDao
import com.example.myprofile.data.room.entity.ContactEntity
import com.example.myprofile.data.room.entity.UserContactJoin
import com.example.myprofile.data.room.entity.UserEntity

@Database(
    entities = [UserEntity::class, ContactEntity::class, UserContactJoin::class],
    version = 1,
    exportSchema = false
)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun contactsDao(): ContactDao
    abstract fun userDao(): UserDao
}
