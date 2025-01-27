package com.bagha.echomemory.B_Function

import android.content.Context
import android.view.View
import android.widget.Toast
import com.bagha.echomemory.R
import com.google.android.material.snackbar.Snackbar

class GlobalFunction {

    fun ShowSnakBar(view: View , message : String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .setAnchorView(R.id.fab).show()
    }

    fun ShowToast (context: Context , message : String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}//end class