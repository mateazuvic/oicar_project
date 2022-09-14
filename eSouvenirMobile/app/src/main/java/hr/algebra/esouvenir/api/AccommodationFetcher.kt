package hr.algebra.esouvenir.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import hr.algebra.esouvenir.TOKEN
import hr.algebra.esouvenir.framework.getStringPreference
import hr.algebra.esouvenir.jsonModel.AccommodationJson
import hr.algebra.esouvenir.jsonModel.PopularItemJson
import hr.algebra.esouvenir.model.AccommodationItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AccommodationFetcher(private val context: Context) {

    private var accApi: AccommodationApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        accApi = retrofit.create(AccommodationApi::class.java)
    }

    fun insertAccommodationItem(qrId: Int, onResult: (AccommodationJson) -> Unit) {
        //val accJson = AccommodationJson(acc.pictures, acc.idAccommodationItem, acc.name, acc.link, acc.description, acc.qrId)
        val request = accApi.insertAccommodation(qrId, token= "Bearer " + context.getStringPreference(
            TOKEN
        )) //poziv rest apija iz backenda
        request.enqueue(object : Callback<AccommodationJson> {
            override fun onResponse(
                call: Call<AccommodationJson>,
                response: Response<AccommodationJson>
            ) {
                response.body()?.let {
                    if (response.isSuccessful) {
                        val message: String = "Successful..."
                        Toast.makeText(context, message + it, Toast.LENGTH_LONG)
                        onResult(it)

                    }

                }
            }

            override fun onFailure(call: Call<AccommodationJson>, t: Throwable) {
                val message: String = t.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_LONG)
                Log.d(javaClass.name, t.message, t)

            }

        })

    }

    fun updateAccommodation(accItem : AccommodationJson) {
        val request = accApi.updateAccommodation(accItem, token= "Bearer " + context.getStringPreference(TOKEN)) //poziv rest apija iz backenda
        request.enqueue(object : Callback<Int> {
            override fun onResponse(
                call: Call<Int>,
                response: Response<Int>
            ) {
                response.body()?.let {
                    if (response.isSuccessful) {
                        val message: String = "Successful..."
                        Toast.makeText(context, message + it, Toast.LENGTH_LONG)


                    }

                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                val message: String = t.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_LONG)
                Log.d(javaClass.name, t.message, t)

            }

        })
    }

    }