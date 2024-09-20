package com.example.myprofile.data.room.repository.impl

import android.util.Log
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.toUserEntity
import com.example.myprofile.data.room.dao.ContactDao
import com.example.myprofile.data.room.dao.UserDao
import com.example.myprofile.data.room.entity.ContactEntity
import com.example.myprofile.data.room.entity.UserContactJoin
import com.example.myprofile.data.room.entity.toUser
import com.example.myprofile.data.room.repository.DatabaseRepository

class DatabaseRepositoryImpl(
    private val userDao: UserDao,
    private val contactDao: ContactDao,
): DatabaseRepository {
    override suspend fun findContactById(contactId: Long): ContactEntity {
        return contactDao.findContactById(contactId)
    }

    override suspend fun getUserContacts(userId: Long): List<User> {
        return contactDao.getUserContacts(userId).contacts.map { it.toUser() }  // .contacts.map { it.toUser() }
    }

    override suspend fun addUserContacts(userId: Long, contacts: List<ContactEntity>) {
        contactDao.insertContacts(contacts)
        val userContacts = contacts.map { UserContactJoin(userId, it.id) }
        contactDao.insertUserContactsJoin(userContacts)
    }

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user.toUserEntity())
    }

}