package com.bagha.echomemory.DB_Room.Model_Table

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Sound")
data class Sound_Table(
    @PrimaryKey(autoGenerate = true) var id: Int ,
    var addressSound: String ,
    var idMemoInfo: Int ,
    ){
    //@NonNull

}//end class