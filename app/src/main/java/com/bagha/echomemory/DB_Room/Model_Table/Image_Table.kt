package com.bagha.echomemory.DB_Room.Model_Table

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Image")
data class Image_Table(
    @PrimaryKey(autoGenerate = true) var id: Int ,
    var addressImage: String ,
    var idMemoInfo: Int ,

    ){
    //@NonNull

}//end class