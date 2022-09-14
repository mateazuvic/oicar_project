package hr.algebra.esouvenir

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hr.algebra.esouvenir.api.GeoLocationFetcher
import hr.algebra.esouvenir.framework.callDelayed
import hr.algebra.esouvenir.framework.getBooleanPreference
import hr.algebra.esouvenir.framework.getIntPreference
import hr.algebra.esouvenir.framework.startActivity
import hr.algebra.esouvenir.model.GeoLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

const val LOCATIONS_IMPORTED="hr.algebra.eSouvenir.locations_imported"
private const val DELAY = 3000L

class GoogleMapLocationsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var locations : MutableList<GeoLocation> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
       // if(!getBooleanPreference(LOCATIONS_IMPORTED)) {      //OVOO PROVJERITII S MOBOM

        addLocations(locations)

       // }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_map_location)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
       callDelayed(DELAY) {mapFragment.getMapAsync(this)} //ovo sam zadnje jer nije radilo (index out of bounds)



    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap

            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear()


                val googlePlex = CameraPosition.builder()
                    .target(LatLng(locations[0].positionX, locations[0].positionY))
                    .zoom(12f) //blizina
                    .bearing(0f) //rotacija
                    .tilt(90f) //kut
                    .build()
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 2800, null)





            locations.forEach {
                mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(it.positionX, it.positionY))
                        .title(it.name)
                        .snippet("")

                )
            }


            mMap.apply {
                uiSettings.isMapToolbarEnabled = true
            }

            mMap.setOnMarkerClickListener { marker ->
                marker.showInfoWindow()
                Toast.makeText(this, "Klik", Toast.LENGTH_SHORT).show()

                true
            }

            mMap.setOnInfoWindowClickListener { marker ->
                //otvori novi frag ili activity
                val loc = locations.find {
                    it.name == marker.title
                }
                startActivity<PopularLocationEdit>(LOCATION_ID, loc?.idGeoLocation!!, LOCATION_NAME, marker.title)
            }



        }

    }

    override fun onPause() {
        super.onPause()
        mMap?.clear()
    }



    private fun addLocations(locations: MutableList<GeoLocation>) {
        //dinamički dodaj s backenda LISTU lokacija za određeni grad
        //GlobalScope.launch {
            GeoLocationFetcher(this).fetchLocations(getIntPreference(ID_CITY)) { list ->
                list?.forEach {
                    locations.add(
                        GeoLocation(
                            it.iDGeoLocation,
                            it.name,
                            it.positionX,
                            it.positionY,
                            it.cityID
                        )
                    )
                }
            }
       // }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<HomeActivity>()
    }


}