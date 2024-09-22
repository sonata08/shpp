package com.example.myprofile.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "user_contact",
    primaryKeys = ["userId", "contactId"],
    indices = [Index("contactId")]
)
data class UserContactJoin(
    @ColumnInfo(name = "userId") val userId: Long,
    @ColumnInfo(name = "contactId") val contactId: Long,
)
