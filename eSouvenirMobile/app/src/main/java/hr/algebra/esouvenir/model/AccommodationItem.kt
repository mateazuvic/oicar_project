package hr.algebra.esouvenir.model

data class AccommodationItem(

    var idAccommodationItem: Int? = null,
    var name: String? = null,
    var link: String? = null,
    var description: String? = null,
    var qrId: Int = 0,
    var pictures: MutableList<Picture>? = null
)