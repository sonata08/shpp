package com.example.myprofile.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.myprofile.data.room.entity.UserEntity

@Dao
interface UserDao {

    @Upsert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun findUserById(userId: Long): UserEntity
}