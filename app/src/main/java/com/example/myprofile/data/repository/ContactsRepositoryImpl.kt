package com.example.myprofile.data.repository

import com.example.myprofile.data.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactsRepositoryImpl : ContactsRepository {

    private val _contactsFlow = MutableStateFlow(contactsList)
    private val contactsFlow = _contactsFlow.asStateFlow()

    private var lastDeletedContact: Pair<Int, Contact>? = null
    override fun getContacts() = contactsFlow

    override fun deleteContact(contactPosition: Int) {
        _contactsFlow.value = _contactsFlow.value.toMutableList().apply {
            lastDeletedContact = contactPosition to get(contactPosition)
            remove(lastDeletedContact?.second)
        }
    }

    override fun restoreLastDeletedContact() {
        _contactsFlow.value = _contactsFlow.value.toMutableList().apply {
            lastDeletedContact?.let {
                add(it.first, it.second)
                lastDeletedContact = null
            }
        }
    }

    override fun addContact(contact: Contact, index: Int) {
        _contactsFlow.value = _contactsFlow.value.toMutableList().apply {
            add(index, contact)
        }
    }

    companion object {
        val contactsList = listOf(
            Contact("Alice", "Engineer", "https://picsum.photos/200?random=1"),
            Contact(
                "John VeryLongNameThatDoesDotFitInTheView",
                "Designer",
                "https://picsum.photos/200?random=2"
            ),
            Contact("Charlie", "Manager", "https://picsum.photos/200?random=3"),
            Contact("David", "Developer", "https://picsum.photos/200?random=4"),
            Contact("Emily", "Artist", "https://picsum.photos/200?random=5"),
            Contact("Frank", "Teacher", "https://picsum.photos/200?random=6"),
            Contact("Grace", "Musician", "https://picsum.photos/200?random=7"),
            Contact("Henry", "Doctor", "https://picsum.photos/200?random=8"),
            Contact("Isabella", "Writer", "https://picsum.photos/200?random=9"),
            Contact("Jack", "Chef", "https://picsum.photos/200?random=10"),
            Contact("Katherine", "Scientist", "https://picsum.photos/200?random=11"),
            Contact("Liam", "Athlete", "https://picsum.photos/200?random=12"),
            Contact("Mia", "Photographer", "https://picsum.photos/200?random=13"),
            Contact("Noah", "Architect", "https://picsum.photos/200?random=14"),
            Contact("Olivia", "Entrepreneur", "https://picsum.photos/200?random=15"),
            Contact("Peter", "Actor", "https://picsum.photos/200?random=16"),
            Contact("Quinn", "Lawyer", "https://picsum.photos/200?random=17"),
            Contact("Ryan", "Pilot", "https://picsum.photos/200?random=18"),
            Contact("Sophia", "Dancer", "https://picsum.photos/200?random=19"),
            Contact("Thomas", "Engineer", "https://picsum.photos/200?random=20"),
            Contact("Alice", "Engineer", "https://picsum.photos/200?random=21"),
            Contact(
                "John VeryLongNameThatDoesDotFitInTheView",
                "Designer",
                "https://picsum.photos/200?random=22"
            ),
            Contact("Charlie", "Manager", "https://picsum.photos/200?random=23")
        )
    }
}