package lk.nibm.holidayviewer

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.icu.util.ULocale.getCountry
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class LocalHolidays : AppCompatActivity() {

    private lateinit var txtHomeCurrentLocation: TextView
    var selectedYear: String = ""
    var selectedCountry: String = ""
    private lateinit var fusedLocation: FusedLocationProviderClient
    var isPermissionGranted: Boolean = false
    private val LOCATION_REQUEST_CODE = 100

    private lateinit var spnYear: Spinner
    private lateinit var btnFilter: Button
    private lateinit var txtLHCountry: TextView
    private lateinit var txtLHYear: TextView
    private lateinit var parentRecyclerView: RecyclerView
    private lateinit var shrimmer : ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_holidays)
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        initializeComponents()

        Handler(Looper.getMainLooper()).postDelayed({
            checkLocationPermission()
        },500)

    }

    private fun initializeComponents() {
        parentRecyclerView = findViewById(R.id.parentRecyclerView)
        txtLHCountry = findViewById(R.id.txtLHCountry)
        spnYear = findViewById(R.id.spnYear)
        btnFilter = findViewById(R.id.btnFilter)
        txtLHYear = findViewById(R.id.txtLHYear)
        shrimmer = findViewById(R.id.shrimmer)
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
            if (isPermissionGranted) {

                getCountry()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCountry() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR).toString()
        if (isPermissionGranted) {
            val locationResult = fusedLocation.lastLocation
            locationResult.addOnCompleteListener(this) { location ->
                if (location.isSuccessful) {
                    val lastLocation = location.result
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses =
                        geocoder.getFromLocation(lastLocation!!.latitude, lastLocation.longitude, 1)
                    val country = addresses?.get(0)!!.countryCode
                    val countryName = addresses?.get(0)!!.countryName
                    selectedCountry = country
                    txtLHCountry.text = countryName
                    if (country != null) {
                        getHolidayData(country, currentYear)
                    } else {
                        getHolidayData("LK", currentYear)
                    }
                } else {
                    getHolidayData("LK", currentYear)
                }
            }
        } else {
            getHolidayData("LK", currentYear)

        }
    }

    private fun getHolidayData(country: String, currentYear: String) {

    }


}