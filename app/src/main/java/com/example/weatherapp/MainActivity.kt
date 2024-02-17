package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val BASE_URL = "https://api.openweathermap.org/"
    private val API_KEY = "api_key"

    private lateinit var textViewTemperature: TextView
    private lateinit var textViewHumidity: TextView
    private lateinit var textViewDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewTemperature = findViewById(R.id.textViewTemperature)
        textViewHumidity = findViewById(R.id.textViewHumidity)
        textViewDescription = findViewById(R.id.textViewDescription)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherApiService::class.java)
        val call = service.getWeather("Moscow", "metric", API_KEY)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        textViewTemperature.text = "Temperature: ${it.main.temperature}°C"
                        textViewHumidity.text = "Humidity: ${it.main.humidity}%"
                        textViewDescription.text = "Description: ${it.weather[0].description}"
                    }
                } else {
                    Log.e("Weather", "Failed to fetch weather data")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("Weather", "Error fetching weather data", t)
            }
        })
    }
}
