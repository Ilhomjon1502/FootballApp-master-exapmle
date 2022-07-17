package com.example.footballapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.footballapp.repository.LeagueRepository

@Suppress("UNCHECKED_CAST")
class LeagueViewModelFactory(
    private val leagueRepository: LeagueRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeagueViewModel::class.java)) {
            return LeagueViewModel(leagueRepository) as T
        }
        throw Exception("Error")
    }
}