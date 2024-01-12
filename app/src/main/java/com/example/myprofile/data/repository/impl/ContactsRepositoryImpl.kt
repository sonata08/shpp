package com.example.myprofile.data.repository.impl

import android.util.Log
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserMultiselect
import com.example.myprofile.data.network.ContactsApiService
import com.example.myprofile.data.network.model.AddContactRequest
import com.example.myprofile.data.network.model.AuthUiStateTest
import com.example.myprofile.data.network.model.BaseResponse
import com.example.myprofile.data.network.model.Contacts
import com.example.myprofile.data.network.model.UserIdTokens
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepositoryImpl @Inject constructor(
    private val contactsApiService: ContactsApiService,
    private val dataStoreRepository: DataStoreRepository,
) : ContactsRepository {


    private val _userContactsFlow = MutableStateFlow<List<UserMultiselect>>(emptyList())
    override val userContactsFlow = _userContactsFlow.asStateFlow()


    private var lastDeletedContact: Long? = null

    override suspend fun getUsers(): AuthUiStateTest<List<User>> {
        val userIdTokens = getUserIdTokens()
        return try {
            val response = contactsApiService.getAllUsers(userIdTokens.accessToken)
            AuthUiStateTest.Success(removeExistingContacts(response.data.users))
        } catch (e: HttpException) {
            Log.d("FAT_ContRep_getUsers_catch", e.toString())
            AuthUiStateTest.Error("")
        }
    }

    override suspend fun getUserContacts(): AuthUiStateTest<List<User>> {
        val userIdTokens = getUserIdTokens()
        return try {
            if (_userContactsFlow.value.isEmpty()) {
                val response =
                    contactsApiService.getAllUserContacts(
                        token = userIdTokens.accessToken,
                        userId = userIdTokens.userId,
                    )
                updateContactsFlow(response.data.contacts)
                AuthUiStateTest.Success(response.data.contacts)
            }
            else {
                AuthUiStateTest.Success(_userContactsFlow.value.map { it.contact })
            }

        } catch (e: HttpException) {
            Log.d("FAT_ContRep_getAllContacts_catch", e.toString())
            AuthUiStateTest.Error(e.toString())
        }
    }

    // contact info for DetailViewFragment
    override suspend fun getContact(contactId: Long): AuthUiStateTest<User> {
        val contact = _userContactsFlow.value.find { it.contact.id == contactId }
        if (contact != null) {
            return AuthUiStateTest.Success(contact.contact)
        }
        val userIdTokens = getUserIdTokens()
        return try {
            val response = contactsApiService.getAllUsers(userIdTokens.accessToken)

            val contactFromServer = response.data.users.find { it.id == contactId }
            if (contactFromServer != null) {
                AuthUiStateTest.Success(contactFromServer)
            } else {
                AuthUiStateTest.Error("no such user")
            }

        } catch (e: Exception) {
            Log.d("FAT_AuthRep_get_catch", "getUser_error = $e")
            AuthUiStateTest.Error(e.message ?: "unknown ERROR")
        }


    }

    override suspend fun deleteContact(contactId: Long): AuthUiStateTest<List<User>> {
        lastDeletedContact = contactId
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            val response =
                contactsApiService.deleteContact(
                    token = userIdTokens.accessToken,
                    userId = userIdTokens.userId,
                    contactId = contactId.toInt()
                )
            updateContactsFlow(response.data.contacts)
            AuthUiStateTest.Success(response.data.contacts)
        } catch (e: HttpException) {
            Log.d("FAT_ContRep_delContact_catch", e.toString())
            AuthUiStateTest.Error("")
        }
    }

    override suspend fun deleteContacts(): AuthUiStateTest<List<User>> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        val list = _userContactsFlow.value
        return try {
            var response = BaseResponse("","", "", Contacts(emptyList()))
            for (user in list) {
                if(user.isSelected) {
                    response = contactsApiService.deleteContact(
                        token = userIdTokens.accessToken,
                        userId = userIdTokens.userId,
                        contactId = user.contact.id.toInt()
                    )
                }
            }
            updateContactsFlow(response.data.contacts)
            AuthUiStateTest.Success(response.data.contacts)
        } catch (e: HttpException) {
            Log.d("FAT_ContRep_delContacts_catch", e.toString())
            AuthUiStateTest.Error("")
        }
    }

    override suspend fun restoreLastDeletedContact() {
        lastDeletedContact?.let {
            addContact(it)
        }
        lastDeletedContact = null
    }

    override suspend fun addContact(
        contactId: Long
    ): AuthUiStateTest<List<User>> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            val response = contactsApiService.addContact(
                token = userIdTokens.accessToken,
                userId = userIdTokens.userId,
                AddContactRequest(contactId.toInt())
            )
            updateContactsFlow(response.data.contacts)
            AuthUiStateTest.Initial
        } catch (e: HttpException) {
            Log.d("FAT_ContRep_addContact_catch", e.toString())
            AuthUiStateTest.Error("")
        }
    }

    override fun makeSelected(contactPosition: Int, isChecked: Boolean) {
        _userContactsFlow.value = _userContactsFlow.value.toMutableList().apply {
            get(contactPosition).isSelected = isChecked
        }
    }

    override fun countSelectedItems(): Int {
        _userContactsFlow.value.apply {
            return filter { it.isSelected }.size
        }
    }

    override fun deactivateMultiselectMode() {
        _userContactsFlow.value = _userContactsFlow.value.map { contact ->
            contact.copy(
                isSelected = false,
                isMultiselectMode = false
            )
        }
    }

    override fun activateMultiselectMode() {
        _userContactsFlow.value =
            _userContactsFlow.value.map { contact -> contact.copy(isMultiselectMode = true) }
    }


    private suspend fun getUserIdTokens(): UserIdTokens {
        return dataStoreRepository.getUserIdTokens()
    }

    /**
     * Filter the list of users to remove existing user's contacts from it
     *
     * @param serverList list of users received from server
     * @return new users list or [serverList] if there is no user's contacts
     */
    private fun removeExistingContacts(serverList: List<User>): List<User> {
        val users = _userContactsFlow.value.map {it.contact}
        return serverList.subtract(users).toList()
    }

    private fun updateContactsFlow(users: List<User>) {
        _userContactsFlow.value = users.map {UserMultiselect(it)}
    }
}