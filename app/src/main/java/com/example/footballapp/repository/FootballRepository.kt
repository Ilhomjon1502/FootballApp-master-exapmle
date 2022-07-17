package com.example.footballapp.repository

import com.example.footballapp.database.FootballDatabase
import com.example.footballapp.model.Data
import com.example.footballapp.network.ApiService

class FootballRepository(
    private val apiService: ApiService,
    private val db: FootballDatabase
) {
    suspend fun saveAllFootballs(dataList: List<Data>) = db.dao().saveLeagues(dataList)
    suspend fun getLocalFootballs() = db.dao().getAllLeagues()

    suspend fun getRemoteFootballs() = apiService.getAllLeagues()
}