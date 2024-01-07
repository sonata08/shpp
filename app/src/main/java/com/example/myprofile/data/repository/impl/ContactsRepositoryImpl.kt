package com.example.myprofile.data.repository.impl

import android.util.Log
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserInfoHolder
import com.example.myprofile.data.network.ContactsApiService
import com.example.myprofile.data.network.model.AuthUiStateTest
import com.example.myprofile.data.network.model.BaseResponse
import com.example.myprofile.data.network.model.Contacts
import com.example.myprofile.data.network.model.UserIdTokens
import com.example.myprofile.data.network.model.Users
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.data.repository.DataStoreRepository
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepositoryImpl @Inject constructor(
    private val contactsApiService: ContactsApiService,
    private val dataStoreRepository: DataStoreRepository,
    private val userInfoHolder: UserInfoHolder
) : ContactsRepository {

    // TODO: maybe remove this line and go to datastore every time we need data from it

    override suspend fun getUsers(): AuthUiStateTest<List<User>> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            val response = contactsApiService.getAllUsers(userIdTokens.accessToken)
            Log.d("FAT_ContRep", "getUsers response done")
            AuthUiStateTest.Success(response.data.users)
        } catch (e: HttpException) {
            Log.d("FAT_ContRep_getUsers_catch", e.toString())
            AuthUiStateTest.Error("")
        }
    }

    override suspend fun getUserContacts(): AuthUiStateTest<List<User>> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            if (userInfoHolder.userContacts.isEmpty()) {
                val response =
                    contactsApiService.getAllUserContacts(
                        token = userIdTokens.accessToken,
                        userId = userIdTokens.userId,
                    )
                Log.d("FAT_ContRep", "getAllContacts response done")
                userInfoHolder.userContacts = response.data.contacts
                AuthUiStateTest.Success(response.data.contacts)
            } else {
                AuthUiStateTest.Success(userInfoHolder.userContacts)
            }

        } catch (e: HttpException) {
            Log.d("FAT_ContRep_getAllContacts_catch", e.toString())
            AuthUiStateTest.Error("")
        }
    }

    override suspend fun deleteContact(contactId: Long): AuthUiStateTest<List<User>> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            val response =
                contactsApiService.deleteContact(
                    token = userIdTokens.accessToken,
                    userId = userIdTokens.userId,
                    contactId = contactId.toInt()
                )
            Log.d("FAT_ContRep", "deleteContact response done")
            userInfoHolder.userContacts = response.data.contacts
            AuthUiStateTest.Success(response.data.contacts)
        } catch (e: HttpException) {
            Log.d("FAT_ContRep_delContact_catch", e.toString())
            AuthUiStateTest.Error("")
        }
    }

    override fun deleteContacts() {
        TODO("Not yet implemented")
    }

    override fun restoreLastDeletedContact() {
        TODO("Not yet implemented")
    }

    override suspend fun addContact(
        contactId: Long
    ): AuthUiStateTest<List<User>> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            val response = contactsApiService.addContact(
                token = userIdTokens.accessToken,
                userId = userIdTokens.userId,
                contactId.toInt()
            )
            Log.d("FAT_ContRep", "addContact response done")
            userInfoHolder.userContacts = response.data.contacts
            AuthUiStateTest.Success(response.data.contacts)
        } catch (e: HttpException) {
            Log.d("FAT_ContRep_addContact_catch", e.toString())
            AuthUiStateTest.Error("")
        }
    }

    override fun makeSelected(contactPosition: Int, isChecked: Boolean) {
        TODO("Not yet implemented")
    }

    override fun countSelected(): Int {
        TODO("Not yet implemented")
    }

    override fun deactivateMultiselectMode() {
        TODO("Not yet implemented")
    }

    override fun activateMultiselectMode() {
        TODO("Not yet implemented")
    }
}