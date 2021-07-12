package com.example.clima.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val url="https://api.openweathermap.org/data/2.5/"

private val moshi= Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit= Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(url)
    .build()

//https://api.openweathermap.org/data/2.5/weather?q=London&appid=710b9a74fd5943aff012c7e3e83be945
interface CiudadApiService{
//    @GET(value= "weather?q=London&appid=710b9a74fd5943aff012c7e3e83be945")
//    fun getTemp(): Call<Weather>

    @GET(value= "weather?q=London&appid=710b9a74fd5943aff012c7e3e83be945")
    suspend fun getTemp2(): Weather

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
