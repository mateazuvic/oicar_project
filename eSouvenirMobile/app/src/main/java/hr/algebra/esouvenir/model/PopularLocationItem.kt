package hr.algebra.esouvenir.model


data class PopularLocationItem(

    var idPopularLocationItem: Int? = null,
    var description: String? = null,
    var geoLocationId: Int = 0,
    var qrCodeID: Int = 0,
    var isVisited: Boolean = false,
    var pictures: MutableList<Picture>? = null

    ) {


}