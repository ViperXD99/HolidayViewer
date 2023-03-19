package lk.nibm.holidayviewer

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class Home : AppCompatActivity() {

    private lateinit var txtHomeCurrentLocation: TextView

    //For getting location
    private lateinit var fusedLocation: FusedLocationProviderClient
    var isPermissionGranted: Boolean = false
    private val LOCATION_REQUEST_CODE = 100
    private var countryCode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initializeComponents()
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()
    }

    private fun initializeComponents() {
        txtHomeCurrentLocation = findViewById(R.id.txtHomeCurrentLocation)

    }


    override fun onStart() {
        checkLocationPermission()
        super.onStart()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {

        if (isPermissionGranted) {
            val locationResult = fusedLocation.lastLocation
            locationResult.addOnCompleteListener(this) { location ->
                if (location.isSuccessful) {
                    val lastLocation = location.result
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses =
                        geocoder.getFromLocation(lastLocation!!.latitude, lastLocation.longitude, 1)
                    val country = addresses?.get(0)!!.countryName
                    txtHomeCurrentLocation.text = country
                    if (country != null) {
                        getCountryId(country)
                    } else {
                        getCurrentMonthHolidays("LK")

                    }
                } else {
                    getCurrentMonthHolidays("LK")

                }
            }
        } else {
            txtHomeCurrentLocation.text = "Location Not Available !"
            getCurrentMonthHolidays("LK")

        }
    }

    fun getCountryId(country: String): Any {
        TODO("Not yet implemented")
    }

    fun getCurrentMonthHolidays(s: String): Any {
        TODO("Not yet implemented")
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_REQUEST_CODE
            )
        } else {
            isPermissionGranted = true
            getCurrentLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionGranted = true
                    getCurrentLocation()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}