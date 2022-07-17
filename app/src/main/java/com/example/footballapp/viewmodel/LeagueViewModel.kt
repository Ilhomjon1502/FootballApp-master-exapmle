package com.example.footballapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footballapp.model.Club
import com.example.footballapp.repository.LeagueRepository
import com.example.footballapp.util.Resource
import kotlinx.coroutines.launch

class LeagueViewModel(
    private val repository: LeagueRepository
) : ViewModel() {
    private val _response: MutableLiveData<Resource<Club>> = MutableLiveData()
    val response: LiveData<Resource<Club>> get() = _response

    fun getLeagueById(id: String) {
        viewModelScope.launch {
            _response.postValue(Resource.loading(null))
            val remoteClubs = repository.getClubById(id)
            if (remoteClubs.isSuccessful) {
                _response.postValue(Resource.success(remoteClubs.body()))
            } else {
                _response.postValue(Resource.error(remoteClubs.errorBody().toString(), null))
            }
        }
    }
}