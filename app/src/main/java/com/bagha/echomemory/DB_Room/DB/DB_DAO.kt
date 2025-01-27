package com.bagha.echomemory.DB_Room.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bagha.echomemory.DB_Room.Model_Table.Image_Table
import com.bagha.echomemory.DB_Room.Model_Table.MemoInfo_Table
import com.bagha.echomemory.DB_Room.Model_Table.Sound_Table

@Dao
interface DB_DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert_memoInfo(vararg memoInfoModel: MemoInfo_Table)

    @Update
    fun update_memoInfo(vararg memoInfoModel: MemoInfo_Table?)

    @Delete
    fun delete_memoInfo(vararg memoInfoModel: MemoInfo_Table)

    @Query("SELECT * FROM MemoInfo")
    fun getAllMemoList(): List<MemoInfo_Table>

    @Query("SELECT * FROM MemoInfo WHERE id=(:id) ")
    fun getMemoryTitleById(id: Int): MemoInfo_Table



     //*************************************
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert_image(vararg imageModel: Image_Table)

    @Update
    fun update_image(vararg imageModel: Image_Table?)

    @Delete
    fun delete_image(vararg imageModel: Image_Table)

    @Query("SELECT * FROM Image")
    fun getAllImage(): List<Image_Table>

    @Query("SELECT * FROM Image WHERE idMemoInfo=(:id) ")
    fun getImageListById(id: Int): List<Image_Table>


     //*************************************
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert_sound(vararg soundModel: Sound_Table)

    @Update
    fun update_sound(vararg soundModel: Sound_Table?)

    @Delete
    fun delete_sound(vararg soundModel: Sound_Table)

    @Query("SELECT * FROM Sound")
    fun getAllSound(): List<Sound_Table>

    @Query("SELECT * FROM Sound WHERE idMemoInfo=(:id) ")
    fun getSoundById(id: Int): Sound_Table



    /*@Insert
    fun insert_address(addressModel: Address_of_user_ModelORFildOfTable)*/

}//end interface