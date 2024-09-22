package com.example.myprofile.data.network.repository.impl

import com.example.myprofile.data.datastore.repository.DataStoreRepository
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserMultiselect
import com.example.myprofile.data.model.toContactEntity
import com.example.myprofile.data.network.NO_USER_ERROR
import com.example.myprofile.data.network.api.ContactsApiService
import com.example.myprofile.data.network.handleApiCall
import com.example.myprofile.data.network.model.BaseResponse
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.model.UserIdTokens
import com.example.myprofile.data.network.model.response_dto.AddContactRequest
import com.example.myprofile.data.network.model.response_dto.Contacts
import com.example.myprofile.data.network.repository.ContactsRepository
import com.example.myprofile.data.room.entity.toUser
import com.example.myprofile.data.room.repository.DatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class ContactsRepositoryImpl @Inject constructor(
    private val contactsApiService: ContactsApiService,
    private val dataStore: DataStoreRepository,
    private val database: DatabaseRepository,
) : ContactsRepository {

    // list of user's contacts
    private val _userContactsFlow = MutableStateFlow<List<UserMultiselect>>(emptyList())
    override val userContactsFlow = _userContactsFlow.asStateFlow()

    private var lastDeletedContact: Long? = null

    override suspend fun getUsers(): UiState<List<User>> {
        val userIdTokens = getUserIdTokens()
        return handleApiCall(
            onApiCall = {
                val response = contactsApiService.getAllUsers(userIdTokens.accessToken)
                val users = response.data.users
                database.insertAll(users.map { it.toContactEntity() })
                UiState.Success(removeExistingContacts(users))
            },
            onConnectException = {
                val users = database.getAllContacts().map { it.toUser() }
                UiState.Success(removeExistingContacts(users))
            }
        )
    }

    override suspend fun getUserContacts(): UiState<List<User>> {
        val userIdTokens = getUserIdTokens()
        return handleApiCall(
            onApiCall = {
                val response =
                    contactsApiService.getAllUserContacts(
                        token = userIdTokens.accessToken,
                        userId = userIdTokens.userId,
                    )
                _userContactsFlow.value = mapToUserMultiselect(response.data.contacts)

                // save user contacts to database
                val contactsForDb = response.data.contacts.map { it.toContactEntity() }
                database.addAllUserContacts(userIdTokens.userId, contactsForDb)
                UiState.Success(response.data.contacts)

            },
            onConnectException = {
                val userContacts = database.getUserContacts(userIdTokens.userId)
                _userContactsFlow.value = mapToUserMultiselect(userContacts)
                UiState.Success(userContacts)
            }
        )
    }

    // contact info for DetailViewFragment
    override suspend fun getContact(contactId: Long): UiState<User> {
        // if contact is clicked from user's contact list
        val contact = _userContactsFlow.value.find { it.contact.id == contactId }
        if (contact != null) return UiState.Success(contact.contact)

        // if contact is clicked from general user's list
        val userIdTokens = getUserIdTokens()

        return handleApiCall(
            onApiCall = {
                val response = contactsApiService.getAllUsers(userIdTokens.accessToken)
                val contactFromServer = response.data.users.find { it.id == contactId }
                    ?: return@handleApiCall UiState.Error(NO_USER_ERROR)
                UiState.Success(contactFromServer)
            },
            onConnectException = {
                val dbContact = database.findContactById(contactId).toUser()
                UiState.Success(dbContact)
            }
        )
    }

    override suspend fun addContact(
        contactId: Long
    ): UiState<List<User>> {
        val userIdTokens = dataStore.getUserIdTokens()
        return handleApiCall(
            onApiCall = {
                val response = contactsApiService.addContact(
                    token = userIdTokens.accessToken,
                    userId = userIdTokens.userId,
                    AddContactRequest(contactId.toInt())
                )
                _userContactsFlow.value = mapToUserMultiselect(response.data.contacts)
                database.addContactToUser(userIdTokens.userId, contactId)
                UiState.Initial
            },
        )
    }

    override suspend fun deleteContact(contactId: Long): UiState<List<User>> {
        lastDeletedContact = contactId
        val userIdTokens = dataStore.getUserIdTokens()
        return handleApiCall(
            onApiCall = {
                val response =
                    contactsApiService.deleteContact(
                        token = userIdTokens.accessToken,
                        userId = userIdTokens.userId,
                        contactId = contactId.toInt()
                    )
                _userContactsFlow.value = mapToUserMultiselect(response.data.contacts)
                database.deleteContact(userIdTokens.userId, contactId)
                UiState.Success(response.data.contacts)
            }
        )
    }

    // delete multiple contacts when multiselect mode
    override suspend fun deleteContacts(): UiState<List<User>> {
        val userIdTokens =  dataStore.getUserIdTokens()
        val list = _userContactsFlow.value
        return handleApiCall(
            onApiCall = {
                var response = BaseResponse(data = Contacts(emptyList()))

                for (user in list) {
                    if (user.isSelected) {
                        response = contactsApiService.deleteContact(
                            token = userIdTokens.accessToken,
                            userId = userIdTokens.userId,
                            contactId = user.contact.id.toInt()
                        )
                        _userContactsFlow.value = mapToUserMultiselect(response.data.contacts)
                        database.deleteContact(userIdTokens.userId,  user.contact.id)
                    }
                }
                UiState.Success(response.data.contacts)
            }
        )
    }

    override suspend fun restoreLastDeletedContact() {
        lastDeletedContact?.let {
            addContact(it)
        }
        lastDeletedContact = null
    }

    override suspend fun logOut() {
        dataStore.logOut()
        _userContactsFlow.value = emptyList()
    }

    override fun updateContactsFlow(users: List<UserMultiselect>) {
        _userContactsFlow.value = users
    }


    private suspend fun getUserIdTokens(): UserIdTokens {
        return dataStore.getUserIdTokens()
    }

    /**
     * Filter the list of users to remove existing user's contacts from it
     *
     * @param serverList list of users received from server
     * @return new users list or [serverList] if there are no user's contacts
     */
    private fun removeExistingContacts(serverList: List<User>): List<User> {
        val users = _userContactsFlow.value.map { it.contact }
        return serverList.subtract(users).toList()
    }

    private fun mapToUserMultiselect(users: List<User>): List<UserMultiselect> {
        return users.map { UserMultiselect(it) }
    }
}