package com.bagha.echomemory.ui.ListMemory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bagha.echomemory.DB_Room.Model_Table.MemoInfo_Table

class TransformViewModel(/*arrayList : List<MemoInfo_Table>*/) : ViewModel() {

    private val _texts = MutableLiveData<List<String>>().apply {
        value = (1..16).mapIndexed { _, i ->
            "This is item # $i"
        }
    }

    val texts: LiveData<List<String>> = _texts


    private val _titleMemo = MutableLiveData<List<MemoInfo_Table>>().apply {
        /*value = (1..arrayList.size-1).mapIndexed { _, i ->
            arrayList[i].title
        }*/
        val Mem =MemoInfo_Table(0 , "tessss" , "12-34-34" , "dd" , "")
        value = (1..2).mapIndexed { _, i ->
            Mem
        }
    }
    val titleMemo : LiveData<List<MemoInfo_Table>> = _titleMemo



    /*private val _coverMemo = MutableLiveData<List<String>>().apply {
        value = (1..arrayList.size-1).mapIndexed { _, i ->
            arrayList[i].imageCover
        }
    }
    val coverMemo : LiveData<List<String>> = _coverMemo*/
}