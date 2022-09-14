package hr.algebra.esouvenir.api

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import hr.algebra.esouvenir.CITY_NAME
import hr.algebra.esouvenir.QRScannerActivity
import hr.algebra.esouvenir.TOKEN
import hr.algebra.esouvenir.framework.getStringPreference
import hr.algebra.esouvenir.model.City
import hr.algebra.esouvenir.model.QRCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CityFetcher(private val context: Context) {

    private var cityApi: CityApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        cityApi = retrofit.create(CityApi::class.java)
    }


    fun insertCity(city: City,  onResult: (Int?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val request = cityApi.insertCity(city, token= "Bearer " + context.getStringPreference(TOKEN))

            request.enqueue(object : Callback<Int> {
                override fun onResponse(
                    call: Call<Int>,
                    response: Response<Int>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            //val message: String = "Successful..."
                            //Toast.makeText(context, message + it, android.widget.Toast.LENGTH_LONG)
                            onResult(it)

                        }
                    } else {
                        response.errorBody().toString()

                    }


                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    val message: String = t.localizedMessage
                    Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG)
                    Log.d(javaClass.name, t.message, t)
                    onResult(null)
                }


            })

        }
    }
}