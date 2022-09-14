package hr.algebra.esouvenir.jsonModel

import com.google.gson.annotations.SerializedName
import hr.algebra.esouvenir.model.Picture

data class PopularItemJson(

    @SerializedName("Pictures") val pictures : List<PictureJson>?,
    @SerializedName("IDPopularLocationItem") val iDPopularLocationItem : Int?,
    @SerializedName("Description") val description : String?,
    @SerializedName("GeoLocationID") val geoLocationID : Int,
    @SerializedName("QRCodeID") val qRCodeID : Int,
    @SerializedName("IsVisited") val isVisited : Boolean

)