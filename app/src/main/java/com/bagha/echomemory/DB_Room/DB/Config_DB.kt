package com.bagha.echomemory.DB_Room.DB

import android.content.Context
import androidx.room.Room

class Config_DB {

    companion object {
        var db_dao : DB_DAO? = null
    }

    fun getDB(context: Context) : DB_DAO {
        if(db_dao != null){
            return db_dao!!
        }
        else{
            db_dao = Room.databaseBuilder(context, DB_Room::class.java, "db_sound_memoirs")
                .allowMainThreadQueries() //Allows room to do operation on main thread, default room run query on Background Thread
                .build()
                .getDB_Dao

            return db_dao!!
        }
    }
}//end class