package com.example.myprofile.data.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.myprofile.data.room.entity.ContactEntity
import com.example.myprofile.data.room.entity.UserEntity
import com.example.myprofile.data.room.entity.UserContactJoin

data class UserWithContacts(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",

        associateBy = Junction(
            value = UserContactJoin::class,
            parentColumn = "userId",
            entityColumn = "contactId",
        )
    )
    val contacts: List<ContactEntity>
)
