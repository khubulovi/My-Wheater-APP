package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.model.Repository
import com.example.myapplication.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataObserve: MutableLiveData<Any> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) :
    ViewModel() {

    private val _icon: MutableLiveData<Int> = MutableLiveData()
    val icon: LiveData<Int> = _icon
    private var isDataSetGeo: Boolean = true

    init {
        getWeatherFromLocalSourceGeo()
    }


    fun getLiveData() = liveDataObserve

    fun getWeatherFromLocalSourceGeo() {
        getDataFromLocalSource(isGeo = true)
        _icon.value = R.drawable.ic_launcher_georgia_foreground
    }

    private fun getWeatherFromLocalSourceWorld() {
        getDataFromLocalSource(isGeo = false)
        _icon.value = R.drawable.ic_launcher_world_foreground
    }

    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isGeo = true)


    private fun getDataFromLocalSource(isGeo: Boolean) {
        liveDataObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataObserve.postValue(
                AppState.Sucsess(
                    if (isGeo) repositoryImpl.getWheatherFromLocaleStorageGeo()
                    else repositoryImpl.getWheatherFromLocaleStorageWorld()
                )
            )
        }.start()
    }
    fun changeGeoToWorld(){
        if(isDataSetGeo)
        {getWeatherFromLocalSourceGeo()}
        else{
            getWeatherFromLocalSourceWorld()
        }.also { isDataSetGeo = !isDataSetGeo }
    }
}
