package hr.algebra.esouvenir.api


import hr.algebra.esouvenir.jsonModel.*
import hr.algebra.esouvenir.model.*
import retrofit2.Call
import retrofit2.http.*

const val API_URL = "https://oicartim04app.azurewebsites.net/"   //http://esouvenir01.eu-central-1.elasticbeanstalk.com/
interface GeoLocationsApi {
    @GET("api/geolocations/getbyid/{id}")
    fun getGeoLocations(@Path("id") cityId: Int, @Header("Authorization") token : String?) : Call<List<GeoLocationJson>>
}

interface RegisterApi {
    @POST("api/login/register")
    fun register(@Body user: UserJson) : Call<Int>
}

interface LoginApi {
    @POST("api/login/login")
    fun login(@Body user: UserJson) : Call<UserJson>
}

interface CityApi {
    @POST("api/cities/insert")
    fun insertCity(@Body city: City, @Header("Authorization") token : String?) : Call<Int>
}

interface QRCodeApi {
    @POST("api/qrcode/insert")
    fun insertQrCode(@Body qr: QRCodeJson, @Header("Authorization") token : String?) : Call<Int>
}

interface PopularLocationsApi {
    @GET("api/popularlocations/{geoid}/{qrid}")
    fun getPopLocation(
        @Path("geoid") geoId: Int,
        @Path("qrid") qrId: Int,
        @Header("Authorization") token : String?
    ): Call<PopularItemJson>

    @POST("api/popularlocations/update")
    fun updatePopLocation(@Body popularItem : PopularItemJson, @Header("Authorization") token : String?) : Call<Int>

}

interface AccommodationApi {
    @POST("api/accommodation/insert/{qrId}")
    fun insertAccommodation(@Path("qrId") qrId: Int, @Header("Authorization") token : String?): Call<AccommodationJson>

    @POST("api/accommodation/update")
    fun updateAccommodation(@Body accItem : AccommodationJson, @Header("Authorization") token : String?): Call<Int>
}

interface PictureApi {
    @POST("api/pictures/insert")
    fun insertPicture(@Body picture: Picture, @Header("Authorization") token : String?) : Call<Int>

    @GET("api/pictures/getbyid/{id}/{cat}")
    fun getPicture(
        @Path("id") id: Int,
        @Path("cat") cat:Int,
        @Header("Authorization") token : String?
    ): Call<PictureJson>
}

interface WalletApi {
    @GET("api/wallet/get")
    fun getWalletItems(@Header("Authorization") token : String?) : Call<List<WalletJson>>

    @DELETE("api/wallet/delete/{id}")
    fun deleteWalletItem(@Path("id") id : Int, @Header("Authorization") token : String?) : Call<String>

    @GET("api/wallet/getById/{id}")
    fun getWalletItem(@Path("id") id : Int, @Header("Authorization") token : String?): Call<WalletJson>

    @POST("api/wallet/insert")
    fun insertWalletItem(@Body walletItem: WalletJson, @Header("Authorization") token : String?): Call<Int>

    @POST("api/wallet/update")
    fun updateWalletItem(@Body walletItem: WalletJson, @Header("Authorization") token : String?): Call<Int>
}