package com.example.myapplication.view.details

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.model.WheatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

private const val YOUR_API_KEY = "a1e32612-e3e3-4d00-a67a-160953ff3620"

@RequiresApi(Build.VERSION_CODES.N)
class WheatherLoader(
    private val listener: WheaterLoaderListener,
    private val lan: Double,
    private val lon: Double
) {
    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWheater() {
        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/informers?lat=${lan}&lon=${lon}")
            val handler = Handler()
            Thread(Runnable {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.addRequestProperty(
                        "X-Yandex-API-Key",
                        YOUR_API_KEY
                    )
                    urlConnection.readTimeout = 10000
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))

                    val weatherDTO: WheatherDTO =
                        Gson().fromJson(getLines(bufferedReader), WheatherDTO::class.java)
                    handler.post { listener.onLoad(weatherDTO) }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                    listener.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }).start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            listener.onFailed(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader) = reader.lines().collect(Collectors.joining("\n"))

    interface WheaterLoaderListener {
        fun onLoad(wheaterDTO: WheatherDTO)
        fun onFailed(throwable: Throwable)
    }
}