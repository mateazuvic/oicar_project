package hr.algebra.esouvenir.jsonModel

import com.google.gson.annotations.SerializedName

data class UserJson(

    //@SerializedName("QRCodes") val qRCodes: List<QRCodeJson>?,
    @SerializedName("IDUser") val iDUser: Int?,
    @SerializedName("Username") val username: String,
    @SerializedName("Email") val email: String?,
    @SerializedName("Pass") val pass: String,
    @SerializedName("IsAdmin") val isAdmin: Boolean,
    @SerializedName("Token") val token: String?
)