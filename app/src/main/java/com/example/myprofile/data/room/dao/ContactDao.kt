package com.example.myprofile.data.room.dao

import androidx.room.Dao
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

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun findContactById(id: Long): ContactEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: List<ContactEntity>)

//    @Query(
//        "SELECT contacts.* FROM contacts " +
//                "INNER JOIN user_contact ON contacts.id = user_contact.contactId " +
//                "WHERE user_contact.userId = :userId"
//    )
//    suspend fun getUserContacts(userId: Long): List<ContactEntity>

    @Transaction
    @Query("select * from users where users.id=:userId")
    suspend fun getUserContacts(userId: Long): UserWithContacts
    @Upsert
    suspend fun insertContacts(userContacts: List<ContactEntity>)

    @Upsert
    suspend fun insertUserContactsJoin(userContacts: List<UserContactJoin>)
}
