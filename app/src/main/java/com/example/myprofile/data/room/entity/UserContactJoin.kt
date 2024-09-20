package com.example.myprofile.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.ForeignKey

@Entity(
    tableName = "user_contact",
//    foreignKeys = [
//        ForeignKey(
//            entity = UserEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["userId"],
//        ),
//        ForeignKey(
//            entity = ContactEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["contactId"],
//        )
//    ],
    primaryKeys = ["userId", "contactId"],
    indices = [Index("contactId")]
)
data class UserContactJoin(
    @ColumnInfo(name = "userId") val userId: Long,
    @ColumnInfo(name = "contactId") val contactId: Long,
)
