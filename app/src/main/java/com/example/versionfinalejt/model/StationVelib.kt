package com.example.versionfinalejt.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StationVelib(
    @PrimaryKey val station_id:Long,
    val name:String,
    val lat:Float,
    val lon:Float,
    val capacity:Int,
    val nbrVelosDispo :Int,
    val nbrDockDispo:Int,
)

