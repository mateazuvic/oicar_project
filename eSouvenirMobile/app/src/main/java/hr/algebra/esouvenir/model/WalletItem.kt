package hr.algebra.esouvenir.model

import java.text.DecimalFormat
import kotlin.math.roundToInt



data class WalletItem (

    var idWalletItem: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var price: Double? = 0.00,
    var qrId: Int? = null,
    var pictures: MutableList<Picture>? = null
) {


}