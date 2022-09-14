package hr.algebra.esouvenir.framework

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import hr.algebra.esouvenir.LOCATIONS_IMPORTED
import hr.algebra.esouvenir.api.GeoLocationFetcher
import hr.algebra.esouvenir.model.GeoLocation
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

inline fun<reified T : Activity> Context.startActivity() //da bi prezivjelo u runtime-u inline i reified
        = startActivity(Intent(this, T::class.java).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
})

//formalno nije ekstenzijska (nije ni nad kime), ali je public static (dostupna svima)
fun callDelayed(delay: Long, function: Runnable) { //proslijedujemo delay i funkciju
    Handler(Looper.getMainLooper()).postDelayed(
        function,
        delay
    )
}

inline fun<reified T : Activity> Context.startActivity(keyString: String, valueString: String) //da bi prezivjelo u runtime-u inline i reified
        = startActivity(Intent(this, T::class.java).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    putExtra(keyString, valueString)
    //putExtra(keyId, valueId)
})

inline fun<reified T : Activity> Context.startActivity(keyString: String, valueInt: Int, key: String, value: String) //da bi prezivjelo u runtime-u inline i reified
        = startActivity(Intent(this, T::class.java).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    putExtra(keyString, valueInt)
    putExtra(key, value)

})

fun Context.setIntPreference(key: String, value: Int)=
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putInt(key, value)
        .apply()


fun Context.getIntPreference(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getInt(key,-1)

fun Context.setBooleanPreference(key: String, value: Boolean)=
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putBoolean(key, value)
        .apply()


fun Context.getBooleanPreference(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key, false)

fun Context.setStringPreference(key: String, value: String)=
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putString(key, value)
        .apply()


fun Context.getStringPreference(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getString(key,"")

fun Context.setDatePreference(key: String, value: LocalDateTime)=
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putString(key, value.format(DateTimeFormatter.ISO_DATE_TIME))
        .apply()


fun Context.getDatePreference(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getString(key,"")

/*
fun Context.addLocations(locations: MutableList<GeoLocation>) : MutableList<GeoLocation> {
    //dinamički dodaj s backenda LISTU lokacija za određeni grad
    GeoLocationFetcher(this).fetchLocations() { list ->
        list?.forEach {
            locations.add(
                GeoLocation(
                    it.iDGeoLocation,
                    it.name,
                    it.positionX,
                    it.positionY,
                    it.cityID)
            )
        }
    }
    setBooleanPreference(LOCATIONS_IMPORTED, true)

    return locations
}*/
