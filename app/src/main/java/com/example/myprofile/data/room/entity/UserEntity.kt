package com.example.myprofile.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myprofile.data.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "career") val career: String?,
    @ColumnInfo(name = "birthday") val birthday: String?,
)

fun UserEntity.toUser(): User {
    return User(
        id = this.id,
        name = this.name,
        address = this.address,
        career = this.career,
        birthday = this.birthday
    )
}