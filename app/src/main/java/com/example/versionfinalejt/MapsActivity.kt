package com.example.versionfinalejt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat


import com.example.versionfinalejt.R

import com.example.versionfinalejt.databinding.ActivityMapsBinding
import com.example.versionfinalejt.api.StationVelibService
import com.example.versionfinalejt.model.MarkerHolder

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

import com.example.versionfinalejt.model.StationVelib
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    val listStationVelib: MutableList<StationVelib> = mutableListOf()
    val markerHolderMap = HashMap<String, MarkerHolder>()

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val paris = LatLng(48.8578, 2.3461)
    private val ileDeFranceBounds = LatLngBounds(
        LatLng(48.7721,2.1634),//Sud-Ouest
        LatLng(48.9384,2.523713)//Nord-Est
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    private val bicycleIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.colorPrimary)
        BitmapHelper.vectorToBitmap(this, R.drawable.ic_directions_bike_black_24dp, color)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paris,11f))
        mMap.setLatLngBoundsForCameraTarget(ileDeFranceBounds)
        mMap.setMinZoomPreference(9.7f)


        synchroApi()
        addMarker()

        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this,markerHolderMap))

    }

    private fun synchroApi() {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://velib-metropole-opendata.smoove.pro/opendata/Velib_Metropole/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()

        val service = retrofit.create(StationVelibService::class.java)

        runBlocking {
            val resultLieu = service.getLieuStation()
            val resultStatus = service.getStatusStation()


            val stationsLieu = resultLieu.data.stations
            val stationsStatus = resultStatus.data.stations

            for (i in stationsLieu) {
                for (j in stationsStatus) {
                    if (i.station_id == j.station_id) {

                        val stationVelib = StationVelib(
                            i.station_id,
                            i.name,
                            i.lat,
                            i.lon,
                            i.capacity,
                            j.num_bikes_available,
                            j.num_docks_available,
                        )
                        listStationVelib.add(stationVelib)
                        break
                    }
                }
            }
        }
    }
        private fun addMarker(){

            listStationVelib.map{
                val coordoneeStation = LatLng(it.lat.toDouble(), it.lon.toDouble())
                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(coordoneeStation)
                        .title(it.name)
                        .snippet(it.station_id.toString())
                        .icon(bicycleIcon)
                )
                val mHolder=MarkerHolder(it.lat,it.lon,it.capacity,it.nbrVelosDispo,it.nbrDockDispo)
                markerHolderMap.put(marker!!.id, mHolder);
            }
        }

}