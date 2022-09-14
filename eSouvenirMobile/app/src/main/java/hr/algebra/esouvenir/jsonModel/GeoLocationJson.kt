package hr.algebra.esouvenir.jsonModel

import com.google.gson.annotations.SerializedName

data class GeoLocationJson (

    @SerializedName("IDGeoLocation") val iDGeoLocation : Int,
    @SerializedName("Name") val name : String,
    @SerializedName("PositionX") val positionX : Double,
    @SerializedName("PositionY") val positionY : Double,
    @SerializedName("CityID") val cityID : Int
)
