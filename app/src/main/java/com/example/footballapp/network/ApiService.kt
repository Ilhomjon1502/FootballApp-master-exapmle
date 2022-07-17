package com.example.footballapp.network

import com.example.footballapp.model.Club
import com.example.footballapp.model.League
import com.example.footballapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(Constants.END_POINT)
    suspend fun getAllLeagues(): Response<League>

    @GET("leagues/{id}/standings")
    suspend fun getClubById(
        @Path("id") id: String,
        @Query("season") season: String = "2021"
    ): Response<Club>
}