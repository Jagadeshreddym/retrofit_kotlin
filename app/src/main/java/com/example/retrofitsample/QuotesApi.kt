package com.example.retrofitsample

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface QuotesApi {
    @GET("/quotes")
    suspend fun getQuotes() : Response<QuoteList>

    @Headers("Content-Type: application/json")
    @POST("/api/v1/create")
    suspend fun createEmployee(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("/api/v1/employees")
    suspend fun getEmployees(): Response<ResponseBody>


    @POST("users")
    // on below line we are creating a method to post our data.
    fun postData(@Body dataModal: DataModal?): Call<DataModal?>?

}
