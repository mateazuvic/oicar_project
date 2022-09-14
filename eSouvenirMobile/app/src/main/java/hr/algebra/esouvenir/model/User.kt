package hr.algebra.esouvenir.model

data class User (

    var _id: Int?,
    val username: String,
    val email: String?,
    val password: String,
    var isAdmin: Boolean,
    var token: String?
    //kolekcija qr kodova??

    )