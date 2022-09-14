package hr.algebra.esouvenir.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import hr.algebra.esouvenir.TOKEN
import hr.algebra.esouvenir.framework.getStringPreference
import hr.algebra.esouvenir.jsonModel.AccommodationJson
import hr.algebra.esouvenir.jsonModel.PictureJson
import hr.algebra.esouvenir.model.Picture
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PictureFetcher(private val context: Context) {

    private var picApi: PictureApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        picApi = retrofit.create(PictureApi::class.java)
    }

    fun insertPicture(picture: Picture, onResult: (Int) -> Unit) {
        val request = picApi.insertPicture(picture, token= "Bearer " + context.getStringPreference(
            TOKEN
        )) //poziv rest apija iz backenda
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

    /*fun getPicture(id: Int, cat:Int, onResult: (PictureJson) -> Unit) {
        val request = picApi.getPicture(id, cat) //poziv rest apija iz backenda
        request.enqueue(object : Callback<PictureJson> {
            override fun onResponse(
                call: Call<PictureJson>,
                response: Response<PictureJson>
            ) {
                response.body()?.let {
                    if (response.isSuccessful) {
                        val message: String = "Successful..."
                        Toast.makeText(context, message + it, Toast.LENGTH_LONG)
                        onResult(it)

                    }

                }
            }

            override fun onFailure(call: Call<PictureJson>, t: Throwable) {
                val message: String = t.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_LONG)
                Log.d(javaClass.name, t.message, t)

            }

        })

    }*/

}