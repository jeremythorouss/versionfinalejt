package com.example.versionfinalejt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.versionfinalejt.api.StationVelibService
import com.example.versionfinalejt.database.AppDatabase
import com.example.versionfinalejt.databinding.ActivityMapsBinding
import com.example.versionfinalejt.model.MarkerHolder
import com.example.versionfinalejt.model.Place
import com.example.versionfinalejt.model.PlaceRenderer
import com.example.versionfinalejt.model.StationVelib
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.UnknownHostException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    val listStationVelib: MutableList<StationVelib> = mutableListOf()
    val markerHolderMap = HashMap<String, MarkerHolder>()
    //val favoris: ArrayList<StationVelib> = ArrayList()

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

        findViewById<Button>(R.id.home_list_favoris).setOnClickListener{
            val intent = Intent(this,ListFavorisActivity::class.java)
            //intent.putExtra("listFavoris",favoris)
            startActivity(intent)

        }



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

        try {
            synchroApi()
        }catch (e: UnknownHostException){
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Pas de connexion, les données velib ne sont pas accessibles ! Accès seulement à la liste des favoris !")
            alertDialogBuilder.setPositiveButton("Yes"){ _, _ ->
                //finish()
            }
            alertDialogBuilder.show()
        }
        addClusteredMarkers(googleMap)
        addMarker()


        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this,markerHolderMap))

        mMap.setOnMarkerClickListener { marker ->
            saveFav(marker)
            false
        }

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

            for(i in stationsLieu){
                for(j in stationsStatus){
                    if(i.station_id==j.station_id){

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
    private fun saveFav(marker: Marker){
        val markerName = marker.title
        findViewById<Button>(R.id.home_add_favoris).setOnClickListener{
            val idStation = marker.snippet
            val stationFav = listStationVelib.find {it.station_id.toString()==idStation}
            if (stationFav != null) {
                //favoris.add(stationFav)
                Toast.makeText(this@MapsActivity, "La station $markerName a été ajouté aux favoris", Toast.LENGTH_SHORT).show()

                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "database-name"
                ).allowMainThreadQueries().build()

                val velibDao = db.stationvelibDao()
                velibDao.insertStation(stationFav)
            }
        }

    }
    /**
     * Adds markers to the map with clustering support.
     */
    private fun addClusteredMarkers(googleMap: GoogleMap) {
        // Create the ClusterManager class and set the custom renderer.
        val clusterManager = ClusterManager<Place>(this, googleMap)
        clusterManager.renderer =
            PlaceRenderer(
                this,
                googleMap,
                clusterManager
            )


        // Add the places to the ClusterManager.
        clusterManager.addItems(place)
        clusterManager.cluster()

        // Set ClusterManager as the OnCameraIdleListener so that it
        // can re-cluster when zooming in and out.
        googleMap.setOnCameraIdleListener {
            clusterManager.onCameraIdle()
        }
    }

}