package com.example.versionfinalejt.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MarkerHolder (
    val lat:Float,
    val lon:Float,
    val capacity:Int,
    val nbrVelosDispo :Int,
    val nbrDockDispo:Int
)