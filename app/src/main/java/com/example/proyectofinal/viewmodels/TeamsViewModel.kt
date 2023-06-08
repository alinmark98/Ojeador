package com.example.proyectofinal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.repositories.TeamsRepository

class TeamsViewModel : ViewModel() {
    private val teamsRepository = TeamsRepository()
    private val regionsLiveData = MutableLiveData<List<String>>()
    private val citiesLiveData = MutableLiveData<List<String>>()
    private val teamsLiveData = MutableLiveData<List<String>>()

    fun getRegions(): LiveData<List<String>> {
        teamsRepository.getRegions { regions ->
            regionsLiveData.postValue(regions)
        }
        return regionsLiveData
    }

    fun getCities(region: String): LiveData<List<String>> {
        teamsRepository.getCities(region) { cities ->
            citiesLiveData.postValue(cities)
        }
        return citiesLiveData
    }

    fun getTeams(region: String, city: String): LiveData<List<String>> {
        teamsRepository.getTeams(region, city) { teams ->
            teamsLiveData.postValue(teams)
        }
        return teamsLiveData
    }
}