package hr.algebra.esouvenir.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonArray
import hr.algebra.esouvenir.TOKEN
import hr.algebra.esouvenir.framework.getStringPreference
import hr.algebra.esouvenir.jsonModel.GeoLocationJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeoLocationFetcher(private val context: Context) {

     var locationApi: GeoLocationsApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        locationApi = retrofit.create(GeoLocationsApi::class.java)
    }

    fun fetchLocations(cityId: Int, onResult: (List<GeoLocationJson>?) -> Unit) {
        val request = locationApi.getGeoLocations(cityId, token= "Bearer " + context.getStringPreference(
            TOKEN)) //poziv rest apija iz backenda
        request.enqueue(object: Callback<List<GeoLocationJson>> {
            override fun onResponse(call: Call<List<GeoLocationJson>>, response: Response<List<GeoLocationJson>>) {
                response.body()?.let {
                    if (response.isSuccessful) {
                        val message: String = "Successful..."
                        Toast.makeText(context, message + it, Toast.LENGTH_LONG)
                        onResult(it)

                    }

                }
            }

            override fun onFailure(call: Call<List<GeoLocationJson>>, t: Throwable) {
                val message: String = t.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_LONG)
                Log.d(javaClass.name, t.message, t)
                onResult(null)
            }

        })


    }






}