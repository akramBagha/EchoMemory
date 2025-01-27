package com.bagha.echomemory.B_Function

import android.content.ContentValues
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileDescriptor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SoundRecord {
     private var mediaRecorder: MediaRecorder? = null

    fun  StopRecord(){
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }
    fun  PauseRecord(){
        mediaRecorder?.apply {
            pause()
            release()
        }
        mediaRecorder = null
    }

    fun StartRecord(context: Context) : String {
        var finalPath = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {//اندروید 10 به بالا
            // ذخیره در مسیر عمومی برای اندروید 10 و بالاتر با استفاده از MediaStore
            finalPath = Directory_Data_Path_forAndroidUper10(context)
            var mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(finalPath)
                prepare()
                start()
            }
        }
        else {
            // ذخیره در حافظه داخلی برای نسخه‌های پایین‌تر از Android 10
            finalPath = Directory_Data_Path_forAndroidLower10(context)

            val mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(finalPath)
                prepare()
                start()
            }
        }
        return finalPath
    }







    fun Directory_Data_Path_forAndroidUper10(context: Context) : String{
        var outputFilePath = ""
        var fileDescriptor: FileDescriptor = FileDescriptor()
        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Audio.Media.DISPLAY_NAME, "Recording_${System.currentTimeMillis()}.mp3")
                put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3")
                put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/Recordings")
            }

            val resolver = context.contentResolver
            val audioUri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)
            fileDescriptor = resolver.openFileDescriptor(audioUri!!, "w")!!.fileDescriptor

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            outputFilePath = "${fileDescriptor}/Recording_$timestamp.mp3"

        }
        catch (e : NullPointerException){
            e.printStackTrace()
        }
        catch (e : Exception){
            e.printStackTrace()
        }
        return outputFilePath

    }

    fun Directory_Data_Path_forAndroidLower10(context: Context) : String{
        var finalPath = ""
        try {
            val outputDir = context.getExternalFilesDir(null)

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            finalPath = "${outputDir?.absolutePath}/Recording_$timestamp.mp3"
        }
        catch (e : NullPointerException){
            e.printStackTrace()
        }
        catch (e : Exception){
            e.printStackTrace()
        }

        Log.i("finalPath" ,"=> "+ finalPath)
        return finalPath

    }

    fun Directory_Music_Path(context: Context) : String {
        var outputFilePath = ""
        try {
            val outputDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            } else {
                File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
                    "MyRecordings"
                )
            }

            if (outputDir != null && !outputDir.exists()) {
                outputDir.mkdirs()
            }
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            outputFilePath = "${outputDir?.absolutePath}/Recording_$timestamp.mp3"
        }
        catch (e : NullPointerException){
            e.printStackTrace()
        }
        catch (e : Exception){

        }

        return outputFilePath
    }


}//end class