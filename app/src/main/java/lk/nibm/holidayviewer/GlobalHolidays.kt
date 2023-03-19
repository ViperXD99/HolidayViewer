package lk.nibm.holidayviewer

import android.icu.util.ULocale.getCountry
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject

class GlobalHolidays : AppCompatActivity() {
    private lateinit var parentRecyclerView: RecyclerView
    private lateinit var shrimmer : ShimmerFrameLayout
    private lateinit var spnYear: Spinner
    private lateinit var btnFilter: Button
    private lateinit var txtLHCountry: TextView
    private lateinit var txtLHYear: TextView
    private var selectedCountry: String = ""
    private var countryId: String? = null
    private lateinit var txtSelectCountry : AutoCompleteTextView
    private val countries = ArrayList<String>()
    private lateinit var fusedLocation: FusedLocationProviderClient
    var isPermissionGranted: Boolean = false
    private val LOCATION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_holidays)
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        initializeComponents()

        Handler(Looper.getMainLooper()).postDelayed({
            getCountries()
            clickListeners()
            getCountry()
        },1000)
    }

    private fun initializeComponents() {
        parentRecyclerView = findViewById(R.id.parentRecyclerView)
        txtLHCountry = findViewById(R.id.txtLHCountry)
        spnYear = findViewById(R.id.spnYear)
        btnFilter = findViewById(R.id.btnFilter)
        txtLHYear = findViewById(R.id.txtLHYear)
        txtSelectCountry= findViewById<AutoCompleteTextView>(R.id.txtSelectCountry)
        shrimmer = findViewById(R.id.shrimmer)
    }

    private fun getCountries() {
        val url =
            resources.getString(R.string.countries_based_url) + resources.getString(R.string.API_Key)
        val resultCountries = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val jsonObjectResponse = jsonObject.getJSONObject("response")
                val jsonArrayCountries = jsonObjectResponse.getJSONArray("countries")
                for (i in 0 until jsonArrayCountries.length()) {
                    val jsonObjectCountry = jsonArrayCountries.getJSONObject(i)
                    countries.add(jsonObjectCountry.getString("country_name"))
                }
                val adapterCountry =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, countries)
                txtSelectCountry.setAdapter(adapterCountry)
                txtSelectCountry.setOnItemClickListener { adapterView, view, i, l ->
                    //Toast.makeText(this,adapterView.getItemAtPosition(i).toString(),Toast.LENGTH_LONG).show()
                    getCountryId(adapterView.getItemAtPosition(i).toString())
                    selectedCountry = adapterView.getItemAtPosition(i).toString()
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
        })
        Volley.newRequestQueue(this).add(resultCountries)
    }

    private fun getCountryId(toString: String) {

    }

    private fun clickListeners() {
        TODO("Not yet implemented")
    }

    private fun getCountry() {
        TODO("Not yet implemented")
    }
}