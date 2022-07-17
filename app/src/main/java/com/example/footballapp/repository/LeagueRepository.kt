package com.example.footballapp.repository

import com.example.footballapp.network.ApiService

class LeagueRepository(
    private val apiService: ApiService
) {
    suspend fun getClubById(id: String) = apiService.getClubById(id)
}