package hr.algebra.esouvenir.model

data class Picture (

    var _id: Int?,
    val name: String,
    val contentType: String?,
    val content: ByteArray?,
    val picturePath: String,
    var accomodationID: Int?,
    var walletID: Int?,
    var popularLocationID: Int?

        ) {

    override fun toString(): String {
        return ""
    }
}

