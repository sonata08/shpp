package com.example.myprofile.data.network.api

import com.example.myprofile.data.network.model.BaseResponse
import com.example.myprofile.data.network.model.response_dto.AddContactRequest
import com.example.myprofile.data.network.model.response_dto.Contacts
import com.example.myprofile.data.network.model.response_dto.Users
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface ContactsApiService {

    @GET("users/")
    suspend fun getAllUsers(
    ): BaseResponse<Users>

    @PUT("users/{userId}/contacts")
    @Headers("Content-type: application/json")
    suspend fun addContact(
       @Path("userId") userId: Long,
       @Body contactId: AddContactRequest
    ): BaseResponse<Contacts>

    @DELETE("users/{userId}/contacts/{contactId}")
    suspend fun deleteContact(
        @Path("userId") userId: Long,
        @Path("contactId") contactId: Int,
    ): BaseResponse<Contacts>

    @GET("users/{userId}/contacts")
    suspend fun getAllUserContacts(
        @Path("userId") userId: Long,
    ): BaseResponse<Contacts>
}