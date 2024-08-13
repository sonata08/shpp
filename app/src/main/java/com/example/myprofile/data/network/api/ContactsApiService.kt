package com.example.myprofile.data.network.api

import com.example.myprofile.data.network.model.BaseResponse
import com.example.myprofile.data.network.model.response_dto.AddContactRequest
import com.example.myprofile.data.network.model.response_dto.Contacts
import com.example.myprofile.data.network.model.response_dto.Users
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface ContactsApiService {

    @GET("users/")
    suspend fun getAllUsers(
        @Header("Authorization") token: String,
    ): BaseResponse<Users>

    @PUT("users/{userId}/contacts")
    @Headers("Content-type: application/json")
    suspend fun addContact(
       @Header("Authorization") token: String,
       @Path("userId") userId: Long,
       @Body contactId: AddContactRequest
    ): BaseResponse<Contacts>

    @DELETE("users/{userId}/contacts/{contactId}")
    suspend fun deleteContact(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Path("contactId") contactId: Int,
    ): BaseResponse<Contacts>

    @GET("users/{userId}/contacts")
    suspend fun getAllUserContacts(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
    ): BaseResponse<Contacts>
}