package com.example.versionfinalejt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.versionfinalejt.dao.StationVelibDao
import com.example.versionfinalejt.model.StationVelib


@Database(entities = [StationVelib::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stationvelibDao(): StationVelibDao
}