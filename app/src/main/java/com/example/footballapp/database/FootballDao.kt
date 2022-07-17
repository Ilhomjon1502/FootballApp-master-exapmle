package com.example.footballapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.footballapp.model.Data

@Dao
interface FootballDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLeagues(leagues: List<Data>)

    @Query("SELECT * FROM football")
    suspend fun getAllLeagues(): List<Data>
}