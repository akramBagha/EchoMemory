package com.bagha.echomemory.DB_Room.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bagha.echomemory.DB_Room.Model_Table.Image_Table
import com.bagha.echomemory.DB_Room.Model_Table.MemoInfo_Table
import com.bagha.echomemory.DB_Room.Model_Table.Sound_Table


@Database(entities = [MemoInfo_Table::class ,
                        Image_Table::class ,
                        Sound_Table::class
                     ] ,
    version = 1)
abstract class DB_Room : RoomDatabase() {
    abstract val getDB_Dao: DB_DAO?
}//end class