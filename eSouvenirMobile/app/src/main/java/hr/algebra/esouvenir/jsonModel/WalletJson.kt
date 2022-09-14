package hr.algebra.esouvenir.jsonModel

import com.google.gson.annotations.SerializedName
import hr.algebra.esouvenir.model.Picture

class WalletJson(
    @SerializedName("Pictures") val pictures: MutableList<PictureJson>?,
    @SerializedName("IDWalletItem") val iDWalletItem: Int?,
    @SerializedName("Name") val name: String?,
    @SerializedName("Description") val description: String?,
    @SerializedName("Price") val price: Double?,
    @SerializedName("QRCodeID") val qRCodeID: Int?
)
