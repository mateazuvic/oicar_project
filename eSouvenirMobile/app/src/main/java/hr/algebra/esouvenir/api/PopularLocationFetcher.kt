package hr.algebra.esouvenir.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import hr.algebra.esouvenir.TOKEN
import hr.algebra.esouvenir.framework.getStringPreference
import hr.algebra.esouvenir.jsonModel.PopularItemJson
import hr.algebra.esouvenir.model.PopularLocationItem
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PopularLocationFetcher(private val context: Context) {

    private var popularApi: PopularLocationsApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        popularApi = retrofit.create(PopularLocationsApi::class.java)
    }

    fun fetchPopularItem(geoId: Int, qrId: Int, onResult: (PopularItemJson) -> Unit) {
        val request = popularApi.getPopLocation(geoId, qrId, token= "Bearer " + context.getStringPreference(
            TOKEN
        )) //poziv rest apija iz backenda
        request.enqueue(object : Callback<PopularItemJson> {
            override fun onResponse(
                call: Call<PopularItemJson>,
                response: Response<PopularItemJson>
            ) {
                response.body()?.let {
                    if (response.isSuccessful) {
                        val message: String = "Successful..."
                        Toast.makeText(context, message + it, Toast.LENGTH_LONG)
                        onResult(it)

                    }

                }
            }

            override fun onFailure(call: Call<PopularItemJson>, t: Throwable) {
                val message: String = t.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_LONG)
                Log.d(javaClass.name, t.message, t)

            }

        })

    }

    fun updatePopularItem(popItem : PopularItemJson) {
        val request = popularApi.updatePopLocation(popItem, token= "Bearer " + context.getStringPreference(TOKEN)) //poziv rest apija iz backenda
        request.enqueue(object : Callback<Int> {
            override fun onResponse(
                call: Call<Int>,
                response: Response<Int>
            ) {
                response.body()?.let {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Update", Toast.LENGTH_LONG).show()


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