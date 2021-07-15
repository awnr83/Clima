package com.example.clima.network


import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val url="https://api.openweathermap.org/data/2.5/"

private val moshi= Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit= Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(url)
    .build()


interface CiudadApiService{
    @GET("weather")
    suspend fun getWeather(
        @Query("q") cityName: String?,
        @Query("appid") ApiToken: String?
    ): Weather
}

object CiudadApi{
    val retrofitService: CiudadApiService by lazy{
        retrofit.create(CiudadApiService::class.java)
    }
}
