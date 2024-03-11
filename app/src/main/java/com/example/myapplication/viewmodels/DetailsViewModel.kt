package com.example.myapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.app.AppState
import com.example.myapplication.model.FactDTO
import com.example.myapplication.model.Wheater
import com.example.myapplication.model.WheatherDTO
import com.example.myapplication.model.getDefaultCity
import com.example.myapplication.repository.DetailsRepository
import com.example.myapplication.repository.DetailsRepositoryIplm
import com.example.myapplication.repository.RemoteDataSourse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val SERVER_ERROR = "Server Error"
private const val REQUEST_ERROR = "Requet to Server error"
private const val CORRUPTED_DATA = "Corrupted data"
class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryIplm(RemoteDataSourse())
) : ViewModel() {

    fun getLiveData()=detailsLiveData

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(lat, lon, callBack)
    }

    private val callBack = object :
        Callback<WheatherDTO> {

        override fun onResponse(call: Call<WheatherDTO>, response: Response<WheatherDTO>) {
            val serverResponse: WheatherDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Eror(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<WheatherDTO>, t: Throwable) {
            detailsLiveData.postValue(AppState.Eror(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: WheatherDTO): AppState {
            val fact = serverResponse.fact
            return if (fact?.temp == null || fact.feelsLike == null || fact.condition.isNullOrEmpty()) {
                AppState.Eror(Throwable(CORRUPTED_DATA))
            } else {
                AppState.Sucsess(convertDtoToModel(serverResponse))
            }
        }
    }
    fun convertDtoToModel(weatherDTO: WheatherDTO): List<Wheater> {
        val fact: FactDTO = weatherDTO.fact!!
        return listOf(Wheater(getDefaultCity(), fact.temp!!, fact.feelsLike!!, fact.condition!!, fact.icon))
    }
}