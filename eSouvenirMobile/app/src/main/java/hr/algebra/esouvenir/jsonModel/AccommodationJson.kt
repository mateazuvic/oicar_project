package hr.algebra.esouvenir.jsonModel

import com.google.gson.annotations.SerializedName
import hr.algebra.esouvenir.model.Picture

data class AccommodationJson(

    @SerializedName("Pictures") val pictures: MutableList<PictureJson>?,
    @SerializedName("IDAccomodationItem") val iDAccomodationItem: Int?,
    @SerializedName("Name") val name: String?,
    @SerializedName("Link") val link: String?,
    @SerializedName("Description") val description: String?,
    @SerializedName("QRCodeID") val qRCodeID: Int,
)