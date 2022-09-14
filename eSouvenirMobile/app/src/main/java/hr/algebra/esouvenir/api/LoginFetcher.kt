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

class LoginFetcher(private val context: Context) {

    private var loginApi: LoginApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        loginApi = retrofit.create(LoginApi::class.java)
    }

    fun loginUser(user: User, onResult: (UserJson?) -> Unit) {

        val uJson: UserJson = UserJson( user._id, user.username, user.email, user.password, user.isAdmin, user.token)
        val request = loginApi.login(uJson)
        request.enqueue(object: Callback<UserJson> {
            override fun onResponse(call: Call<UserJson>, response: Response<UserJson>) {
                response.body()?.let {
                    if (response.isSuccessful) {
                        val message: String = "Successful..."
                        Toast.makeText(context, message, Toast.LENGTH_LONG)
                        //val id = response.body()
                        onResult(it)

                    }
                    else {
                        response.errorBody().toString()
                    }
                }
            }

            override fun onFailure(call: Call<UserJson>, t: Throwable) {
                val message: String = t.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_LONG)
                Log.d(javaClass.name, t.message, t)

            }

        })


    }
}