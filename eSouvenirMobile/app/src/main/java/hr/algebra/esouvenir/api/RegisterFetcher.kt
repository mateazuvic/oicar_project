package hr.algebra.esouvenir.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import hr.algebra.esouvenir.jsonModel.UserJson
import hr.algebra.esouvenir.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterFetcher(private val context: Context) {

    private var registerApi: RegisterApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        registerApi = retrofit.create(RegisterApi::class.java)
    }

    fun insertUser(user: User, onResult: (Int?) -> Unit) {

           val uJson: UserJson = UserJson(user._id, user.username, user.email, user.password, user.isAdmin, user.token)
            val request = registerApi.register(uJson)
            request.enqueue(object: Callback<Int>{
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    response.body()?.let {
                        if (response.isSuccessful) {
                            val message: String = "Successful..."
                            Toast.makeText(context, message, Toast.LENGTH_LONG)
                            val id = response.body()
                            onResult(id)

                        }
                        else {
                            response.errorBody().toString()
                        }
                    }
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    val message: String = t.localizedMessage
                    Toast.makeText(context, message, Toast.LENGTH_LONG)
                    Log.d(javaClass.name, t.message, t)
                    onResult(null)
                }

            })


    }
}