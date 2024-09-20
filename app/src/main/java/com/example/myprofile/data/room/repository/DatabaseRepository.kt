package com.example.myprofile.data.room.repository

import com.example.myprofile.data.model.User
import com.example.myprofile.data.room.entity.ContactEntity

interface DatabaseRepository {
    suspend fun findContactById(contactId: Long): ContactEntity
    suspend fun getUserContacts(userId: Long): List<User>
    suspend fun addUserContacts(userId: Long, contacts: List<ContactEntity>)
    suspend fun insertUser(user: User)
}