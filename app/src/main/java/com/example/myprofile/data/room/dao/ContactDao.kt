package com.example.myprofile.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.myprofile.data.room.UserWithContacts
import com.example.myprofile.data.room.entity.ContactEntity
import com.example.myprofile.data.room.entity.UserContactJoin

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<ContactEntity>

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun findContactById(id: Long): ContactEntity

    @Transaction
    @Query("select * from users where users.id=:userId")
    suspend fun getUserContacts(userId: Long): UserWithContacts

    @Upsert
    suspend fun insertContacts(contacts: List<ContactEntity>)

    @Upsert
    suspend fun insertUserContactsJoin(userContacts: List<UserContactJoin>)

    @Delete
    suspend fun deleteContact(contact: UserContactJoin)
}
