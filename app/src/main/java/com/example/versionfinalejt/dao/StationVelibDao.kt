package com.example.versionfinalejt.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.versionfinalejt.model.StationVelib

@Dao
interface StationVelibDao {
    @Query("SELECT * FROM stationvelib")
    fun getAll(): List<StationVelib>

    @Query("SELECT * FROM stationvelib WHERE station_id = (:stationId)")
    fun loadById(stationId:Long): StationVelib

    /*@Query("SELECT * FROM stationvelib WHERE fav='true'")
    fun loadAllFav(): List<StationVelib>

    @Insert
    fun insertStation(stations:List<StationVelib>)*/

    @Insert
    fun insertStation(station:StationVelib)



}