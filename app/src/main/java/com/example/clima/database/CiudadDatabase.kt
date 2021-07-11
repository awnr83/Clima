package com.example.clima.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Ciudad::class], version= 2)
abstract class CiudadDatabase : RoomDatabase() {

    abstract val ciudadDatabaseDao: CiudadDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: CiudadDatabase? = null
        fun getInstance(context: Context): CiudadDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CiudadDatabase::class.java,
                        "ciudades"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}


/*
@Database(entities = [CiudadEntity::class], version= 1)
abstract class CiudadDatabase:RoomDatabase() {
    abstract val ciudadDao: CiudadDatabaseDao
}

private lateinit var INSTANCE: CiudadDatabase

fun getDatabase(context: Context): CiudadDatabase{
    synchronized(CiudadDatabase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE= Room.databaseBuilder(
                context.applicationContext,
                CiudadDatabase::class.java,
                "ciudades"
            ).build()
        }
    }
    return INSTANCE
}*/