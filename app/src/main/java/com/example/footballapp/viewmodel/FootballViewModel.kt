package com.example.footballapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footballapp.model.League
import com.example.footballapp.repository.FootballRepository
import com.example.footballapp.util.NetworkHelper
import com.example.footballapp.util.Resource
import kotlinx.coroutines.launch

class FootballViewModel(
    private val footballRepository: FootballRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    private val _response: MutableLiveData<Resource<League>> = MutableLiveData()
    val response: LiveData<Resource<League>> get() = _response

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _response.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val remoteLeagues = footballRepository.getRemoteFootballs()
                if (remoteLeagues.isSuccessful) {
                    footballRepository.saveAllFootballs(remoteLeagues.body()!!.data)
                    _response.postValue(Resource.success(remoteLeagues.body()))
                } else {
                    _response.postValue(Resource.error(remoteLeagues.errorBody().toString(), null))
                }
            } else {
                _response.postValue(
                    Resource.success(
                        League(
                            footballRepository.getLocalFootballs(),
                            true
                        )
                    )
                )
            }
        }
    }
}