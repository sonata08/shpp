package com.example.myprofile.data.room.repository

import com.example.myprofile.data.model.User
import com.example.myprofile.data.room.entity.ContactEntity

interface DatabaseRepository {
    suspend fun getAllContacts(): List<ContactEntity>
    suspend fun findContactById(contactId: Long): ContactEntity
    suspend fun getUserContacts(userId: Long): List<User>
    suspend fun findUserById(userId: Long): User
    suspend fun insertUser(user: User)
    suspend fun insertAll(contacts: List<ContactEntity>)
    suspend fun addContactToUser(userId: Long, contactId: Long)
    suspend fun addAllUserContacts(userId: Long, contacts: List<ContactEntity>)
    suspend fun deleteContact(userId: Long, contactId: Long)
}