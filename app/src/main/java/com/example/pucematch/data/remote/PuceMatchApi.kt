package com.example.pucematch.data.remote

import com.example.pucematch.data.local.StudentEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Contrato Retrofit para la API de PuceMatch.
 * Define los endpoints del backend que alimentan la capa local (Room).
 */
interface PuceMatchApi {

    @GET("api/v1/students")
    suspend fun getGlobalCatalog(): Response<List<StudentEntity>>

    @POST("api/v1/matches/chat")
    suspend fun sendMessage(@Body message: MessageRequest): Response<Unit>
}
