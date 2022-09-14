package hr.algebra.esouvenir.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import hr.algebra.esouvenir.TOKEN
import hr.algebra.esouvenir.framework.getStringPreference
import hr.algebra.esouvenir.jsonModel.AccommodationJson
import hr.algebra.esouvenir.jsonModel.GeoLocationJson
import hr.algebra.esouvenir.jsonModel.WalletJson
import hr.algebra.esouvenir.model.WalletItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WalletFetcher(private val context: Context) {

    private var wApi: WalletApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        wApi = retrofit.create(WalletApi::class.java)
    }

    fun fetchItems(onResult: (List<WalletJson>?) -> Unit) {
        val request = wApi.getWalletItems(token= "Bearer " + context.getStringPreference(TOKEN)) //poziv rest apija iz backenda
        request.enqueue(object : Callback<List<WalletJson>> {
            override fun onResponse(
                call: Call<List<WalletJson>>,
                response: Response<List<WalletJson>>
            ) {
                response.body()?.let {
                    if (response.isSuccessful) {
                        val message: String = "Successful..."
                        Toast.makeText(context, message + it, Toast.LENGTH_LONG)
                        onResult(it)


                    }

                }
            }

            override fun onFailure(call: Call<List<WalletJson>>, t: Throwable) {
                val message: String = t.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_LONG)
                Log.d(javaClass.name, t.message, t)
                onResult(null)
            }

        })


    }

    fun deleteItem(id: Int) {
        val request = wApi.deleteWalletItem(id, token= "Bearer " + context.getStringPreference(TOKEN)) //poziv rest apija iz backenda
        request.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                response.body()?.let {
                    if (response.isSuccessful) {
                        val message: String = "Successful..."
                        Toast.makeText(context, message + it, Toast.LENGTH_LONG)

                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                val message: String = t.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_LONG)
                Log.d(javaClass.name, t.message, t)

            }

        })


    }

    fun getWalletItem(id: Int, onResult: (WalletJson) -> Unit) {
        val request = wApi.getWalletItem(id, token= "Bearer " + context.getStringPreference(TOKEN)) //poziv rest apija iz backenda
        request.enqueue(object : Callback<WalletJson> {
            override fun onResponse(
                call: Call<WalletJson>,
                response: Response<WalletJson>
            ) {
                response.body()?.let {
                    if (response.isSuccessful) {
                        val message: String = "Successful..."
                        Toast.makeText(context, message + it, Toast.LENGTH_LONG)
                        onResult(it)

                    }

                }
            }

            override fun onFailure(call: Call<WalletJson>, t: Throwable) {
                val message: String = t.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_LONG)
                Log.d(javaClass.name, t.message, t)

            }

        })

    }

    fun insertWalletItem(item: WalletJson, onResult: (Int) -> Unit) {
        val request = wApi.insertWalletItem(item, token= "Bearer " + context.getStringPreference(TOKEN)) //poziv rest apija iz backenda
        request.enqueue(object : Callback<Int> {
            override fun onResponse(
                call: Call<Int>,
                response: Response<Int>
            ) {
                response.body()?.let {
                    if (response.isSuccessful) {
                        val message: String = "Successful..."
                        Toast.makeText(context, message + it, Toast.LENGTH_LONG)
                        onResult(it)

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

    fun updateWalletItem(item: WalletJson) {
        val request = wApi.updateWalletItem(item, token= "Bearer " + context.getStringPreference(TOKEN)) //poziv rest apija iz backenda
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