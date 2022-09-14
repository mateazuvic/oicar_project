package hr.algebra.esouvenir.model

data class GeoLocation (

    var idGeoLocation: Int?,
    val name: String,
    val positionX: Double,
    val positionY: Double,
    var cityid: Int

    )