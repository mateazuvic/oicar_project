package hr.algebra.esouvenir.jsonModel

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class QRCodeJson(

    @SerializedName("IDQRCode") val iDQRCode: Int?,
    @SerializedName("CreationDate") val creationDate: LocalDateTime?,
    @SerializedName("UserID") val userID: Int,
    @SerializedName("CityID") val cityID: Int
)

