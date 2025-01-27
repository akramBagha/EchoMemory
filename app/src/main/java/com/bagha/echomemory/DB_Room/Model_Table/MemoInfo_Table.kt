package com.bagha.echomemory.DB_Room.Model_Table

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "MemoInfo")
data class MemoInfo_Table(
    @PrimaryKey(autoGenerate = true) var id: Int ,
    var title: String ,
    var createDate: String ,
    var imageCover: String ,
    var description: String ,

    ){
    //@NonNull

}//end class