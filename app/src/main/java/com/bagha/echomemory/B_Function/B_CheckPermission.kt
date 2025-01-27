package com.bagha.echomemory.B_Function

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat

class B_CheckPermission {
    var READ_STORAGE_REQUEST_CODE = 100

    fun checkPermissions_RECORD_AUDIO(context : Context): Boolean {
        val recordPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {//Sdk =29 & android=10
            val writePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val readPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            recordPermission == PackageManager.PERMISSION_GRANTED &&
                    writePermission == PackageManager.PERMISSION_GRANTED &&
                    readPermission == PackageManager.PERMISSION_GRANTED
        } else {
            recordPermission == PackageManager.PERMISSION_GRANTED
        }


        return ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED

    }






    fun checkPermissions_Media(activity: Activity) : Boolean{
        if(Build.VERSION.SDK_INT <= 32){
            requestPermissionStorage(activity)
            return  (setStoragePermission(activity))
        }
        else if(Build.VERSION.SDK_INT > 32){
            requestPermissionMediaImage(activity)
            return  (setMedeaImagePermission(activity))
        }
        else{
            return false
        }
    }
    //_________ requestPermissionStorage
    private fun requestPermissionStorage(activity: Activity) {
        requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE ,Manifest.permission.WRITE_EXTERNAL_STORAGE ),
            READ_STORAGE_REQUEST_CODE
        )
    }
    //_________ requestPermissionMediaImage
    private fun requestPermissionMediaImage(activity: Activity) {
        requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES ),
            READ_STORAGE_REQUEST_CODE
        )
    }

    //_______________ setStoragePermission
    private fun setStoragePermission(activity: Activity): Boolean {
        val permission_READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"
        val permission_WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE"

        val res_READ_EXTERNAL_STORAGE =  activity!!.checkCallingOrSelfPermission(permission_READ_EXTERNAL_STORAGE)
        val res_WRITE_EXTERNAL_STORAGE =  activity!!.checkCallingOrSelfPermission(permission_WRITE_EXTERNAL_STORAGE)

        return (
                (res_READ_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED)
                        &&
                        (res_WRITE_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED)
                )
    }

    //_______________ setMedeaImagePermission
    private fun setMedeaImagePermission(activity: Activity): Boolean {
        val permission_READ_MEDIA_IMAGES = "android.permission.READ_MEDIA_IMAGES"
        val res_READ_MEDIA_IMAGES =  activity!!.checkCallingOrSelfPermission(permission_READ_MEDIA_IMAGES)
        return ( res_READ_MEDIA_IMAGES == PackageManager.PERMISSION_GRANTED)
    }




}//end class