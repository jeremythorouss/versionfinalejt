package com.example.versionfinalejt.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import java.io.Serializable
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
