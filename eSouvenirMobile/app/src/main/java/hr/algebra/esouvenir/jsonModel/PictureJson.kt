package hr.algebra.esouvenir.jsonModel

import com.google.gson.annotations.SerializedName

data class PictureJson (
    @SerializedName("IDPicture") val iDPicture : Int?,
    @SerializedName("Name") val name : String,
    @SerializedName("ContentType") val contentType : String?,
    @SerializedName("Content") val content : ByteArray?,
    @SerializedName("PicturePath") val picturePath : String,
    @SerializedName("AccomodationID") val accomodationID : Int?,
    @SerializedName("PopularLocationID") val popularLocationID : Int?,
    @SerializedName("WalletID") val walletID : Int?

        )