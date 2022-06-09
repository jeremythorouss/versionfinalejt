package com.example.versionfinalejt


import android.os.Bundle

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.versionfinalejt.database.AppDatabase


private const val TAG = "DetailsStationVelibActivity"

class DetailsStationVelibActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_station_velib)

        val idStationSel = intent.getLongExtra("idStation", -1)

        val tvNameStation = findViewById<TextView>(R.id.title)
        val tvIdStation = findViewById<TextView>(R.id.snippet)
        val tvCapacity = findViewById<TextView>(R.id.capacity)
        val tvNbrVelosDispo = findViewById<TextView>(R.id.nbrVelosDispo)
        val tvNbrDockDispo = findViewById<TextView>(R.id.nbrDockDispo)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()

        val velibDao = db.stationvelibDao()
        val stationSelectionee=velibDao.loadById(idStationSel)

        tvNameStation.text=stationSelectionee.name
        tvIdStation.text= stationSelectionee.station_id.toString()
        tvCapacity.text=stationSelectionee.capacity.toString()
        tvNbrVelosDispo.text=stationSelectionee.nbrVelosDispo.toString()
        tvNbrDockDispo.text=stationSelectionee.nbrDockDispo.toString()

    }
}