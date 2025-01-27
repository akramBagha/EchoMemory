package com.bagha.echomemory.B_Function

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.AppCompatImageView
import com.bagha.echomemory.R
import java.io.IOException

class B_SelectImage (activity_: Activity) {
    lateinit var activity : Activity
    var imageBitmap: Bitmap? = null

    init {
        this.activity = activity_
    }
    companion object{
        var mCurrentPhotoPath = ""
    }


    fun SelecetImageMassage_2(
        activity: Activity,
        ProgressBar_attachImage: ProgressBar?,
        onActivityResult : ActivityResultLauncher<Intent>
    ) {
        selectImageFromGallery_2(onActivityResult)
        /*B_AlertDialogManager.showAlertMessage_isTrue(activity ,
            activity.getString(R.string.doYouChooseFromGallery) ,
            object  : B_AlertDialogManager.ActionAfterAlert{
                override fun isTrue(isTrue: Boolean) {
                    if(isTrue){
                        selectImageFromGallery_2(onActivityResult)
                    }
                }

                override fun selectItem(position: Int) {
                    TODO("Not yet implemented")
                }
            })*/
    }


    fun selectImageFromGallery_2(onActivityResult : ActivityResultLauncher<Intent>)  {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("image/*")
        intent!!.putExtra("requestCode" , 300)
        onActivityResult.launch(intent)
        /*onActivityResult.launch(Intent.createChooser(
            intent,
            activity.getString(R.string.selectImageFromGallery))
        )*/
    }


    fun onActivityResult_2(
        data: Intent?,
        perview: AppCompatImageView?
    ): Boolean {
        var result = false
        if(data != null){
            Log.i("IIImage_5" , data!!.data.toString())
        }
        try {
            //_____ Gallery
            if (data != null) {
                val selectedImage: Uri? = data.data
                val filePathColumn = arrayOf<String>(MediaStore.Images.Media.DATA)
                val cursor: Cursor? = activity.getContentResolver().query(
                    selectedImage!!,
                    filePathColumn, null, null, null
                )
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                mCurrentPhotoPath = cursor.getString(columnIndex)
                println("mCurrentPhotoPath => $mCurrentPhotoPath ==")
                cursor.close()

                result = setPic(perview)

            }
        } catch (e: NullPointerException) {
            SetMessage(
                activity.getString(R.string.error),
                "1_:" + e.message
            )
        } catch (e: Exception) {
            SetMessage(
                activity.getString(R.string.error),
                "2_:" + e.message
            )
        }
        return result
    }


    private fun SetMessage(title: String, message: String) {
        B_AlertDialogManager.showAlertMessage(
            activity,
            title,
            message,
            activity.getString(R.string.ok)
        )
    }

    fun setPic(perview: AppCompatImageView?): Boolean {
        var result = false
        imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
        if (rotateTakeImage(mCurrentPhotoPath)) {
            result = true
            try {
                if(perview != null){
                    perview!!.visibility = View.VISIBLE
                    perview?.setImageBitmap(imageBitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    fun rotateTakeImage(photoPath: String): Boolean {
        var result = false
        try {
            val ei = ExifInterface(photoPath)
            val orientation: Int = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            imageBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(imageBitmap!!, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(imageBitmap!!, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(imageBitmap!!, 270f)
                ExifInterface.ORIENTATION_NORMAL -> imageBitmap
                else -> imageBitmap
            }
            result = true
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.getWidth(), source.getHeight(),
            matrix, true
        )
    }










    fun getCorrectedBitmap(filePath: String): Bitmap? {
        try {
            BitmapFactory.Options().apply {
                inSampleSize = 2
            }

            val bitmap = BitmapFactory.decodeFile(filePath)

            val exif = ExifInterface(filePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

            val rotation = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }

            return if (rotation != 0f) {
                val matrix = Matrix()
                matrix.postRotate(rotation)
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }



}//end class