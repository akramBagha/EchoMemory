package com.bagha.echomemory.B_Function

import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView

class B_SetImageView {

    fun SetImageByPathString(activity: Activity, pathImage: String, imageView : AppCompatImageView){
        var imageBitmap = BitmapFactory.decodeFile(pathImage)
        //imageView.setImageBitmap(imageBitmap)

        val ss = B_SelectImage(activity)
        imageBitmap = ss.getCorrectedBitmap(pathImage)
        imageView.setImageBitmap(imageBitmap)
        if (imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap)
        } else {
            Log.e("ImageError", "image not change")
        }
    }
}//end class