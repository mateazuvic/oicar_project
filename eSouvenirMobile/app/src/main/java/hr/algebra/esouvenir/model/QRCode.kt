package hr.algebra.esouvenir.model

import java.time.LocalDateTime
import java.util.*

data class QRCode (

    var idQRCode: Int?,
    val creationDate: LocalDateTime?,
    var userID: Int,
    var cityID: Int

    //kolekcije pop loc items, acc items, wallet items?


        )