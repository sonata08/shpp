package com.example.myprofile.ui.main.contacts.adapter

import android.util.Log
import com.example.myprofile.data.network.repository.ContactsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MultiselectManager @Inject constructor(
    private val contactsRepository: ContactsRepository
) {

    fun makeSelected(contactId: Long, isChecked: Boolean) {
        Log.d("FAT_MultMan", "makeSelected1: $contactId, isChecked = $isChecked")
        val list = contactsRepository.userContactsFlow.value.map { user ->
            if (user.contact.id == contactId) {
                user.copy(isSelected = isChecked)
            } else {
                user
            }}

        contactsRepository.updateContactsFlow(list)
        Log.d("FAT_MultMan", "makeSelected2: ${list.filter { it.isSelected }.joinToString { it.contact.id.toString() }}")
    }

    fun countSelectedItems(): Int {
        contactsRepository.userContactsFlow.value.apply {
            Log.d("FAT_MultMan", "size: ${filter { it.isSelected }.size}")
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