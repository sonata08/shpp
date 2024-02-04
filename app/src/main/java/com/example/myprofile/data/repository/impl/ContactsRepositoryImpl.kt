package com.example.myprofile.data.repository.impl

import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.ContactsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactsRepositoryImpl : ContactsRepository {

    private val _contactsFlow = MutableStateFlow(contactsList)

    private var lastDeletedContact: Pair<Int, Contact>? = null

    override fun getContacts() = _contactsFlow.asStateFlow()

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
            Contact(1, "Alice", "Engineer", "https://picsum.photos/200?random=1"),
            Contact(
                2,
                "John VeryLongNameThatDoesDotFitInTheView",
                "Designer",
                "https://picsum.photos/200?random=2"
            ),
            Contact(3, "Charlie", "Manager", "https://picsum.photos/200?random=3"),
            Contact(4, "David", "Developer", "https://picsum.photos/200?random=4"),
            Contact(5, "Emily", "Artist", "https://picsum.photos/200?random=5"),
            Contact(6, "Frank", "Teacher", "https://picsum.photos/200?random=6"),
            Contact(7, "Grace", "Musician", "https://picsum.photos/200?random=7"),
            Contact(8, "Henry", "Doctor", "https://picsum.photos/200?random=8"),
            Contact(9, "Isabella", "Writer", "https://picsum.photos/200?random=9"),
            Contact(10, "Jack", "Chef", "https://picsum.photos/200?random=10"),
            Contact(11, "Katherine", "Scientist", "https://picsum.photos/200?random=11"),
            Contact(12, "Liam", "Athlete", "https://picsum.photos/200?random=12"),
            Contact(13, "Mia", "Photographer", "https://picsum.photos/200?random=13"),
            Contact(14, "Noah", "Architect", "https://picsum.photos/200?random=14"),
            Contact(15, "Olivia", "Entrepreneur", "https://picsum.photos/200?random=15"),
            Contact(16, "Peter", "Actor", "https://picsum.photos/200?random=16"),
            Contact(17, "Quinn", "Lawyer", "https://picsum.photos/200?random=17"),
            Contact(18, "Ryan", "Pilot", "https://picsum.photos/200?random=18"),
            Contact(19, "Sophia", "Dancer", "https://picsum.photos/200?random=19"),
            Contact(20, "Thomas", "Engineer", "https://picsum.photos/200?random=20"),
            Contact(21, "Alice", "Engineer", "https://picsum.photos/200?random=21"),
            Contact(
                22,
                "John VeryLongNameThatDoesDotFitInTheView",
                "Designer",
                "https://picsum.photos/200?random=22"
            ),
            Contact(23, "Charlie", "Manager", "https://picsum.photos/200?random=23")
        )
    }
}