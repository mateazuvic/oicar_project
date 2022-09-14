package hr.algebra.esouvenir.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import hr.algebra.esouvenir.TOKEN
import hr.algebra.esouvenir.framework.getStringPreference
import hr.algebra.esouvenir.jsonModel.QRCodeJson
import hr.algebra.esouvenir.model.QRCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class QrCodeFetcher(private val context: Context) {

    private var qrApi: QRCodeApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        qrApi = retrofit.create(QRCodeApi::class.java)
    }


    fun insertQrCode(qrCode: QRCode,  onResult: (Int?) -> Unit) {
        val qrJson: QRCodeJson = QRCodeJson(qrCode.idQRCode, qrCode.creationDate, qrCode.userID, qrCode.cityID)
        val request = qrApi.insertQrCode(qrJson, token= "Bearer " + context.getStringPreference(
            TOKEN
        ))
        request.enqueue(object: Callback<Int> {
            override fun onResponse(
                call: Call<Int>,
                response: Response<Int>
            ) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        val message: String = "Successful..."
                        Toast.makeText(context, message + it, Toast.LENGTH_LONG)
                        val id = response.body()
                        onResult(id)
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