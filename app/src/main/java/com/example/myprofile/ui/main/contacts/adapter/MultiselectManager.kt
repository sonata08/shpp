package com.example.myprofile.ui.main.contacts.adapter

import com.example.myprofile.data.network.repository.ContactsRepository
import javax.inject.Inject

/**
 * Manages the multiselect mode for user contacts.
 *
 * @property contactsRepository The repository for managing user contacts.
 */
class MultiselectManager @Inject constructor(
    private val contactsRepository: ContactsRepository
) {

    fun makeSelected(contactPosition: Int, isChecked: Boolean) {
        val list = contactsRepository.userContactsFlow.value.toMutableList().apply {
            this[contactPosition] = get(contactPosition).copy(isSelected = isChecked)
        }.toList()
        contactsRepository.updateContactsFlow(list)
    }

    fun countSelectedItems(): Int {
        contactsRepository.userContactsFlow.value.apply {
            return filter { it.isSelected }.size
        }
    }

    fun deactivateMultiselectMode() {
        val list = contactsRepository.userContactsFlow.value.map { contact ->
            contact.copy(
                isSelected = false,
                isMultiselectMode = false
            )
        }
        contactsRepository.updateContactsFlow(list)
    }

    fun activateMultiselectMode() {
        val list = contactsRepository.userContactsFlow.value.map { contact -> contact.copy(isMultiselectMode = true) }
        contactsRepository.updateContactsFlow(list)
    }
}