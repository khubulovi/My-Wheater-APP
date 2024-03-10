package com.example.myapplication.repository

import com.example.myapplication.model.WheatherDTO
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
private const val YOUR_API_KEY = "a1e32612-e3e3-4d00-a67a-160953ff3620"
class RemoteDataSourse {
    private val wheatherAPI =
        Retrofit.Builder().baseUrl("https://api.weather.yandex.ru/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            ).client(createOkHttpClient(WheatherApiInterceptor()))
            .build().create(WheatherAPI::class.java)


    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }
    class WheatherApiInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }
    fun getWeatherDetails(lat: Double, lon: Double, callback: Callback<WheatherDTO>) {
        wheatherAPI.getWheater(YOUR_API_KEY, lat, lon).enqueue(callback)
    }
}