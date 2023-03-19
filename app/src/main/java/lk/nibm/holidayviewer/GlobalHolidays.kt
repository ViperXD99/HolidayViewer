package lk.nibm.holidayviewer

import android.annotation.SuppressLint
import android.icu.util.ULocale.getCountry
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import lk.nibm.holidayviewer.adaptor.MHAdapter
import lk.nibm.holidayviewer.model.MHModel
import lk.nibm.holidayviewer.model.SeparatedHolidaysModel
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class GlobalHolidays : AppCompatActivity() {
    private val parentList = ArrayList<MHModel>()
    private val childList1 = ArrayList<SeparatedHolidaysModel>()
    private val childList2 = ArrayList<SeparatedHolidaysModel>()
    private val childList3 = ArrayList<SeparatedHolidaysModel>()
    private val childList4 = ArrayList<SeparatedHolidaysModel>()
    private val childList5 = ArrayList<SeparatedHolidaysModel>()
    private val childList6 = ArrayList<SeparatedHolidaysModel>()
    private val childList7 = ArrayList<SeparatedHolidaysModel>()
    private val childList8 = ArrayList<SeparatedHolidaysModel>()
    private val childList9 = ArrayList<SeparatedHolidaysModel>()
    private val childList10 = ArrayList<SeparatedHolidaysModel>()
    private val childList11 = ArrayList<SeparatedHolidaysModel>()
    private val childList12 = ArrayList<SeparatedHolidaysModel>()
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

    private fun getCountryId(countryName: String) {
        val url =
            resources.getString(R.string.countries_based_url) + resources.getString(R.string.API_Key)
        val resultCountries = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val jsonObjectResponse = jsonObject.getJSONObject("response")
                val jsonArrayCountries = jsonObjectResponse.getJSONArray("countries")
                for (i in 0 until jsonArrayCountries.length()) {
                    val jsonObjectCountry = jsonArrayCountries.getJSONObject(i)
                    if (jsonObjectCountry.getString("country_name") == countryName) {
                        countryId = jsonObjectCountry.getString("iso-3166").toString()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
        })
        Volley.newRequestQueue(this).add(resultCountries)
    }

    private fun clickListeners() {
        TODO("Not yet implemented")
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

    private fun getHolidayData(countryCode: String, selectedYear: String) {
        val url =
            resources.getString(R.string.holidays_based_url) + resources.getString(R.string.API_Key) + "&country=" + countryCode + "&year=" + selectedYear
        val result = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {

                val jsonObject = JSONObject(response)
                val jsonObjectResponse = jsonObject.getJSONObject("response")
                val jsonArrayHolidays = jsonObjectResponse.getJSONArray("holidays")
                shrimmer.stopShimmer()
                shrimmer.visibility = View.GONE
                for (i in 0 until jsonArrayHolidays.length()) {
                    val jsonObjectHolidayList = jsonArrayHolidays.getJSONObject(i)
                    val date = jsonObjectHolidayList.getJSONObject("date")
                    val dateTime = date.getJSONObject("datetime")
                    val month = dateTime.getString("month")
                    val holidayPrimaryType = jsonObjectHolidayList.getString("primary_type")
                    val holidayDescription = jsonObjectHolidayList.getString("description")
                    val holidayName = jsonObjectHolidayList.getString("name")
                    val holidayDay = dateTime.getString("day")
                    val holidayMonth = dateTime.getString("month")
                    if (month == "1") {

                        childList1.add(
                            SeparatedHolidaysModel(
                                holidayName,
                                holidayDay,
                                holidayMonth,
                                holidayPrimaryType,
                                holidayDescription
                            )
                        )
                    } else if (month == "2") {

                        childList2.add(
                            SeparatedHolidaysModel(
                                holidayName,
                                holidayDay,
                                holidayMonth,
                                holidayPrimaryType,
                                holidayDescription
                            )
                        )
                    } else if (month == "3") {

                        childList3.add(
                            SeparatedHolidaysModel(
                                holidayName,
                                holidayDay,
                                holidayMonth,
                                holidayPrimaryType,
                                holidayDescription
                            )
                        )
                    } else if (month == "4") {

                        childList4.add(
                            SeparatedHolidaysModel(
                                holidayName,
                                holidayDay,
                                holidayMonth,
                                holidayPrimaryType,
                                holidayDescription
                            )
                        )
                    } else if (month == "5") {

                        childList5.add(
                            SeparatedHolidaysModel(
                                holidayName,
                                holidayDay,
                                holidayMonth,
                                holidayPrimaryType,
                                holidayDescription
                            )
                        )
                    } else if (month == "6") {

                        childList6.add(
                            SeparatedHolidaysModel(
                                holidayName,
                                holidayDay,
                                holidayMonth,
                                holidayPrimaryType,
                                holidayDescription
                            )
                        )
                    } else if (month == "7") {

                        childList7.add(
                            SeparatedHolidaysModel(
                                holidayName,
                                holidayDay,
                                holidayMonth,
                                holidayPrimaryType,
                                holidayDescription
                            )
                        )
                    } else if (month == "8") {

                        childList8.add(
                            SeparatedHolidaysModel(
                                holidayName,
                                holidayDay,
                                holidayMonth,
                                holidayPrimaryType,
                                holidayDescription
                            )
                        )
                    } else if (month == "9") {

                        childList9.add(
                            SeparatedHolidaysModel(
                                holidayName,
                                holidayDay,
                                holidayMonth,
                                holidayPrimaryType,
                                holidayDescription
                            )
                        )
                    } else if (month == "10") {

                        childList10.add(
                            SeparatedHolidaysModel(
                                holidayName,
                                holidayDay,
                                holidayMonth,
                                holidayPrimaryType,
                                holidayDescription
                            )
                        )
                    } else if (month == "11") {

                        childList11.add(
                            SeparatedHolidaysModel(
                                holidayName,
                                holidayDay,
                                holidayMonth,
                                holidayPrimaryType,
                                holidayDescription
                            )
                        )
                    } else if (month == "12") {

                        childList12.add(
                            SeparatedHolidaysModel(
                                holidayName,
                                holidayDay,
                                holidayMonth,
                                holidayPrimaryType,
                                holidayDescription
                            )
                        )
                    }
                }
                parentList.add(MHModel("January", childList1))
                parentList.add(MHModel("February", childList2))
                parentList.add(MHModel("March", childList3))
                parentList.add(MHModel("April", childList4))
                parentList.add(MHModel("May", childList5))
                parentList.add(MHModel("June", childList6))
                parentList.add(MHModel("July", childList7))
                parentList.add(MHModel("August", childList8))
                parentList.add(MHModel("September", childList9))
                parentList.add(MHModel("October", childList10))
                parentList.add(MHModel("November", childList11))
                parentList.add(MHModel("December", childList12))
                // Pass holidays to adapter
                val adapter = MHAdapter(this, parentList)
                parentRecyclerView.setHasFixedSize(true)
                parentRecyclerView.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                parentRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()

        })
        Volley.newRequestQueue(this).add(result)    }
}