package com.example.versionfinalejt.api


import retrofit2.http.GET

interface StationVelibService {
    @GET("station_information.json")
    suspend fun getLieuStation():  GetLieuStationResult

    @GET("station_status.json")
    suspend fun getStatusStation(): GetStatusStationResult

}



data class GetLieuStationResult(val data:StationsLieuResult)
data class StationsLieuResult(val stations : List<StationVelibLieu>)
data class StationVelibLieu(val station_id:Long, val name:String, val lat:Float, val lon:Float, val capacity:Int)

data class GetStatusStationResult(val data:StationsStatusResult)
data class StationsStatusResult(val stations:List<StationVelibStatus>)
data class StationVelibStatus(val station_id:Long, val num_bikes_available :Int, val num_docks_available:Int)
