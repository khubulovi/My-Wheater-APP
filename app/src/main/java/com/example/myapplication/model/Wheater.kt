package com.example.myapplication.model

import android.os.Parcel
import android.os.Parcelable


@Suppress("DEPRECATION")
data class Wheater(
    val city: City = getDefaultCity(),
    val temperature: Int = 0,
    val feelsLike: Int = 0,
    val condition : String = "Sunny",
    val icon : String? = "bkn-n"
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable<City>(City::class.java.classLoader) ?: getDefaultCity(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(city,flags)
        parcel.writeInt(temperature)
        parcel.writeInt(feelsLike)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Wheater> {
        override fun createFromParcel(parcel: Parcel): Wheater {
            return Wheater(parcel)
        }

        override fun newArray(size: Int): Array<Wheater?> {
            return arrayOfNulls(size)
        }
    }
}

fun getDefaultCity() = City("Tbilisi", 43.21342, 22.34124)


fun getWorldCities(): List<Wheater> = listOf(
        Wheater(City("London", 51.5085300, -0.1257400), 1, 2),
        Wheater(City("Tokio", 35.6895000, 139.6917100), 3, 4),
        Wheater(City("Paris", 48.8534100, 2.3488000), 5, 6),
        Wheater(City("Berlin", 52.52000659999999, 13.404953999999975), 7, 8),
        Wheater(City("Rome", 41.9027835, 12.496365500000024), 9, 10),
        Wheater(City("Minsk", 53.90453979999999, 27.561524400000053), 11, 12),
        Wheater(City("Istambul", 41.0082376, 28.97835889999999), 13, 14),
        Wheater(City("Washington", 38.9071923, -77.03687070000001), 15, 16),
        Wheater(City("Kiev", 50.4501, 30.523400000000038), 17, 18),
        Wheater(City("Pekin", 39.90419989999999, 116.40739630000007), 19, 20)
    )


fun getGeoCities(): List<Wheater> =
    listOf(
        Wheater(City("Tbilisi", 51.5085300, -0.1257400), 1, 2),
        Wheater(City("Batumi", 35.6895000, 139.6917100), 3, 4),
        Wheater(City("Gori", 48.8534100, 2.3488000), 5, 6),
        Wheater(City("Rustavi", 52.52000659999999, 13.404953999999975), 7, 8),
        Wheater(City("Telavi", 41.9027835, 12.496365500000024), 9, 10),
        Wheater(City("Gudauri", 53.90453979999999, 27.561524400000053), 11, 12),
        Wheater(City("Kazbegi", 41.0082376, 28.97835889999999), 13, 14),
        Wheater(City("Zugdidi", 38.9071923, -77.03687070000001), 15, 16),
        Wheater(City("Mtskheta", 50.4501, 30.523400000000038), 17, 18),
        Wheater(City("Kutaisi", 39.90419989999999, 116.40739630000007), 19, 20)
    )
