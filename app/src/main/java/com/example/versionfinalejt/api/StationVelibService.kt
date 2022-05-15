package com.example.versionfinalejt.api

import com.example.versionfinalejt.model.StationVelib
import retrofit2.http.GET

interface StationVelibService{
    @GET("station_status.json")
    suspend fun getServiceStation():dataservice
}
data class dataservice(val data: getServiceStationResult)
data class getServiceStationResult(val stations : List<StationVelib>)
//data class StationVelib(val station_id:Int,val name:String,val lat:Float,val lon:Float)