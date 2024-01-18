package com.example.myprofile.data.network.repository.impl

import android.util.Log
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserMultiselect
import com.example.myprofile.data.network.NO_USER_ERROR
import com.example.myprofile.data.network.UNKNOWN_ERROR
import com.example.myprofile.data.network.api.ContactsApiService
import com.example.myprofile.data.network.model.AddContactRequest
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.data.network.model.BaseResponse
import com.example.myprofile.data.network.model.Contacts
import com.example.myprofile.data.network.model.UserIdTokens
import com.example.myprofile.data.network.repository.ContactsRepository
import com.example.myprofile.data.repository.DataStoreRepository
import com.example.myprofile.utils.getMessageFromHttpException
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

    // list of user's contacts
    private val _userContactsFlow = MutableStateFlow<List<UserMultiselect>>(emptyList())
    override val userContactsFlow = _userContactsFlow.asStateFlow()

    private var lastDeletedContact: Long? = null

    override suspend fun getUsers(): UiState<List<User>> {
        val userIdTokens = getUserIdTokens()
        return try {
            val response = contactsApiService.getAllUsers(userIdTokens.accessToken)
            UiState.Success(removeExistingContacts(response.data.users))
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            UiState.Error(getMessageFromHttpException(error))
        } catch (e: Exception) {
            UiState.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun getUserContacts(): UiState<List<User>> {
        val userIdTokens = getUserIdTokens()
        return try {
            if (_userContactsFlow.value.isEmpty()) {
                val response =
                    contactsApiService.getAllUserContacts(
                        token = userIdTokens.accessToken,
                        userId = userIdTokens.userId,
                    )
                updateContactsFlow(response.data.contacts)
                UiState.Success(response.data.contacts)
            }
            else {
                UiState.Success(_userContactsFlow.value.map { it.contact })
            }

        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            UiState.Error(getMessageFromHttpException(error))
        } catch (e: Exception) {
            UiState.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    // contact info for DetailViewFragment
    override suspend fun getContact(contactId: Long): UiState<User> {
        // if contact is clicked from user's contact list
        val contact = _userContactsFlow.value.find { it.contact.id == contactId }
        if (contact != null) return UiState.Success(contact.contact)

        // if contact is clicked from general user's list
        val userIdTokens = getUserIdTokens()
        return try {
            val response = contactsApiService.getAllUsers(userIdTokens.accessToken)

            val contactFromServer = response.data.users.find { it.id == contactId }
            if (contactFromServer != null) {
                UiState.Success(contactFromServer)
            } else {
                UiState.Error(NO_USER_ERROR)
            }
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            UiState.Error(getMessageFromHttpException(error))
        } catch (e: Exception) {
            UiState.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun deleteContact(contactId: Long): UiState<List<User>> {
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
            UiState.Success(response.data.contacts)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            UiState.Error(getMessageFromHttpException(error))
        } catch (e: Exception) {
            UiState.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    // delete multiple contacts when multiselect mode
    override suspend fun deleteContacts(): UiState<List<User>> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        val list = _userContactsFlow.value
        return try {
            var response = BaseResponse(data = Contacts(emptyList()))
            for (user in list) {
                if(user.isSelected) {
                    response = contactsApiService.deleteContact(
                        token = userIdTokens.accessToken,
                        userId = userIdTokens.userId,
                        contactId = user.contact.id.toInt()
                    )
                    updateContactsFlow(response.data.contacts)
                }
            }
            UiState.Success(response.data.contacts)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            UiState.Error(getMessageFromHttpException(error))
        } catch (e: Exception) {
            UiState.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun restoreLastDeletedContact() {
        lastDeletedContact?.let {
            addContact(it)
        }
        lastDeletedContact = null
    }

    override suspend fun logOut() {
        dataStoreRepository.logOut()
        _userContactsFlow.value = emptyList()
    }

    override suspend fun addContact(
        contactId: Long
    ): UiState<List<User>> {
        val userIdTokens = dataStoreRepository.getUserIdTokens()
        return try {
            val response = contactsApiService.addContact(
                token = userIdTokens.accessToken,
                userId = userIdTokens.userId,
                AddContactRequest(contactId.toInt())
            )
            updateContactsFlow(response.data.contacts)
            UiState.Initial
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            UiState.Error(getMessageFromHttpException(error))
        } catch (e: Exception) {
            UiState.Error(e.message ?: UNKNOWN_ERROR)
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