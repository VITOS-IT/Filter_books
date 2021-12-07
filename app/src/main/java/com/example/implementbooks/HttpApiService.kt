package com.example.implementbooks

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface HttpApiService {

    @GET("/books")
    suspend fun getBookList(): List<IpResultBook>
}