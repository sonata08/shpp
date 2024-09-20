package com.example.myprofile.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myprofile.data.model.User

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "career") val career: String?,
)

fun ContactEntity.toUser(): User {
    return User(
        id = this.id,
        name = this.name,
        address = this.address,
        career = this.career,
    )
}