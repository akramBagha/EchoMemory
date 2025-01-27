package com.bagha.echomemory.B_Function

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VoiceRecord {

    fun CreateName(): String{
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return  timestamp
    }
}//end class