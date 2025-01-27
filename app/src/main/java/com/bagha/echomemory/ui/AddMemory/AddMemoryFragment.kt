package com.bagha.echomemory.ui.AddMemory

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bagha.echomemory.B_Function.B_CheckPermission
import com.bagha.echomemory.B_Function.SoundRecord
import com.bagha.echomemory.R
import android.media.AudioRecord
import android.media.MediaPlayer
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import com.bagha.echomemory.Adapter.Adapter_MakeImageAlbum
import com.bagha.echomemory.B_Function.B_AlertDialogManager
import com.bagha.echomemory.B_Function.B_SelectImage
import com.bagha.echomemory.B_Function.B_SelectImage.Companion.mCurrentPhotoPath
import com.bagha.echomemory.B_Function.B_SharedPreferences
import com.bagha.echomemory.B_Function.GlobalFunction
import com.bagha.echomemory.B_Function.VoiceRecord
import java.io.File
import java.io.FileOutputStream
import com.bagha.echomemory.B_Function.GifImageCall
import com.bagha.echomemory.DB_Room.Model_Table.Image_Table
import com.bagha.echomemory.DB_Room.Model_Table.Sound_Table
import com.bagha.echomemory.MainActivity
import com.bagha.echomemory.databinding.FragmentAddMemoryBinding


class AddMemoryFragment : Fragment() {

    private var _bindingAddMemory: FragmentAddMemoryBinding? = null

    private var voicePath = ""
    private lateinit var imageOption : B_SelectImage

    //Chornometer
    private val handlerChornimeter = Handler(Looper.getMainLooper())
    private var startTime: Long = 0
    private var elapsedTime: Long = 0

    //_________
    private var isRecording = false
    var mediaPlayer = MediaPlayer()
    private lateinit var audioRecord: AudioRecord
    private lateinit var recordingThread: Thread
    private val handler = Handler(Looper.getMainLooper())
    private val bufferSize = AudioRecord.getMinBufferSize(
        44100,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )
    private val buffer = ByteArray(bufferSize)
    private val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var rawFile: File
    private lateinit var wavFile: File
    private var output: String = ""
    //_________

    private val bindingAddMemory get() = _bindingAddMemory!!

    lateinit var gifIamgeCall :  GifImageCall

    private var soundHeightForAnim = 2
    var arrayListImageForAddNewMemoVoic : MutableList<String> = ArrayList()
    lateinit var adapter : Adapter_MakeImageAlbum
    var idMemoTitle = -1



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val reflowViewModel =ViewModelProvider(this).get(ReflowViewModel::class.java)

        _bindingAddMemory = FragmentAddMemoryBinding.inflate(inflater, container, false)
        val root: View = bindingAddMemory.root

        try {
            ShowMessaegFirstOpenApp()
            imageOption = B_SelectImage(requireActivity())
            //___List____
            AddSpinnerMemoryList()
            gifIamgeCall = GifImageCall(requireContext())
            //soundHeightForAnim = binding.viewForWavSound.LinearLayoutBackSound.height

            CreatFileName()
            onClickBtn()
        }
        catch (e : NullPointerException){
            e.printStackTrace()
        }
        catch (e : Exception){
            e.printStackTrace()
        }








        return root
    }

    private fun ShowMessaegFirstOpenApp() {
        val firstOpenApp = B_SharedPreferences.preferences!!
            .getBoolean(B_SharedPreferences.perferance_label_firstOpenApp , false )
        if(firstOpenApp){
            bindingAddMemory.CardViewShowMessageForFirstOpenApp.visibility =View.GONE
        }
        else{
            bindingAddMemory.CardViewShowMessageForFirstOpenApp.visibility =View.VISIBLE
            B_SharedPreferences.preferences!!.edit()
                .putBoolean(B_SharedPreferences.perferance_label_firstOpenApp, true).commit()
        }
    }

    private fun onClickBtn() {

        bindingAddMemory.Chronometer.setOnChronometerTickListener {
            if (startTime == 0L) {
                startTime = System.currentTimeMillis()
            }
        }

        //************StartRecordingVoice
        bindingAddMemory.AppCompatImageViewRecord.setOnClickListener(View.OnClickListener {
            if (B_CheckPermission().checkPermissions_RECORD_AUDIO(requireContext())) {
                StartRecordingVoice()
            } else {
                requestPermissions_RECORD_AUDIO()
            }
        })
        //**********StopRecordingVoice
        bindingAddMemory.AppCompatImageViewStop.setOnClickListener(View.OnClickListener {
            StopRecordingVoice()
        })
        //**********PauseRecordingVoice
        bindingAddMemory.AppCompatImageViewPause.setOnClickListener(View.OnClickListener {
            StartRecordingVoice()
        })
        //**********PlayVoice
        bindingAddMemory.AppCompatImageViewPlay.setOnClickListener(View.OnClickListener {
            StartPlayVoice()

        })

        //**********add_image_to_album
        bindingAddMemory.CardViewAddImageToAlbumBtn.setOnClickListener(View.OnClickListener {
            if(B_CheckPermission().checkPermissions_Media(requireActivity())){
                B_SelectImage(requireActivity()).SelecetImageMassage_2(
                    requireActivity(),
                    null ,
                    onActivityResult
                )
            }
        })


        //***********AddNewMemoryWithVoice
        bindingAddMemory.ButtonAddNewMemoryWithVoice.setOnClickListener(View.OnClickListener {
            if(idMemoTitle != -1 && wavFile.exists()){

                if(arrayListImageForAddNewMemoVoic.size > 0){
                    for (i in arrayListImageForAddNewMemoVoic.indices){
                        var image = Image_Table(0 , arrayListImageForAddNewMemoVoic[i] , idMemoTitle)
                        MainActivity.DB.insert_image(image)
                    }
                }

                var sound = Sound_Table(0 , wavFile.path , idMemoTitle)
                MainActivity.DB.insert_sound(sound)

                GlobalFunction().ShowToast(requireContext() , getString(R.string.successSave))
            }
            else{
                B_AlertDialogManager.showAlertMessage(requireActivity() , "ERROR" , requireContext().getString(R.string.saveVoiceAndSelectTitle) , requireContext().getString(R.string.ok))
            }
        })
    }


    

    private fun AddSpinnerMemoryList() {
        try {
            addItemsOn_SpinnerMemoryList()
            if (MainActivity.memoryInfoList == null || MainActivity.memoryInfoList.isEmpty()) {
                Log.e("Spinner", "memoryInfoList is null or empty after rotation")
            } else {
                // مقداردهی Spinner
            }
            if(bindingAddMemory.SpinnerMemoryList != null){
                bindingAddMemory.SpinnerMemoryList.selectedItemPosition
                bindingAddMemory.SpinnerMemoryList?.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long,
                        ) {
                            if (view == null) {
                                Log.e("Spinner", "View is null")
                                return
                            }
                            idMemoTitle  = MainActivity.memoryInfoList[position].id
                            Log.i("Spinner", idMemoTitle.toString())
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
            }
        }
        catch (e : NullPointerException){
            e.printStackTrace()
        }
        catch (e : Exception){
            e.printStackTrace()
        }

    }

    private fun addItemsOn_SpinnerMemoryList() {
        if(MainActivity.memoryInfoList != null && MainActivity.memoryInfoList!!.size > 0){
            var list = ArrayList<String>()
            for (i in MainActivity.memoryInfoList!!.indices) {
                list.add(MainActivity.memoryInfoList!![i].title)
            }
            val dataAdapter =
                ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_design_text, list
                )
            dataAdapter.setDropDownViewResource(R.layout.spinner_design_text)
            bindingAddMemory.SpinnerMemoryList!!.adapter = dataAdapter
        }
    }


    //______________ Gallery_Action
    private val onActivityResult : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            if (imageOption!!.onActivityResult_2(
                    result.data,
                    //binding.AppCompatImageViewCaverMemoir
                    null
                )
            ) {
                arrayListImageForAddNewMemoVoic.add(mCurrentPhotoPath)
                println("imageList_size => $mCurrentPhotoPath == ${arrayListImageForAddNewMemoVoic.size}")
                if(arrayListImageForAddNewMemoVoic.size == 1){
                    adapter = Adapter_MakeImageAlbum(requireActivity() , arrayListImageForAddNewMemoVoic)
                    //binding.makeImageAlbum.recyclerviewAlbum.setLayoutManager(LinearLayoutManager(requireContext()))
                    bindingAddMemory.makeImageAlbum.recyclerviewAlbum.adapter = adapter
                }
                else if(arrayListImageForAddNewMemoVoic.size > 1){
                    adapter.notifyDataSetChanged()
                }
            }

        }
        else {
            // binding.ProgressBarAttachImage.setVisibility(View.GONE)
        }
    }











    override fun onStop() {
        super.onStop()
        /*if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }*/
        if(mediaPlayer.isPlaying)
            mediaPlayer.setOnCompletionListener {
                it.release()
            }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _bindingAddMemory = null
        handlerChornimeter.removeCallbacksAndMessages(null)
    }


































    private fun StartRecordingVoice() {
        if (isRecording) {
            StopRecordingVoice()
            //PauseRecrdingVoice()
        }
        else {
            StartRecordingAndSave()
        }
    }
    /*private fun ResumeRecordingVoice() {
        if (isRecording) {
            PauseRecrdingVoice()
        } else {
            //ResumeRecordingAndSave()
        }
    }*/

    private fun StartRecordingAndSave() {

       CreatFileName()
        //output = Environment.getExternalStorageDirectory().absolutePath + "/recording.mp3"



        //__Start Chornometer
        startTime = System.currentTimeMillis() - elapsedTime
        StartTimer(bindingAddMemory.TextViewChornometer)
        bindingAddMemory.Chronometer.start()

        //outputFilePath = SoundRecord().StartRecord(requireContext())
        isRecording = true

        StartAnimationMicrophon()
        RunSoundWaveAnimation()
        GlobalFunction().ShowToast(requireContext() , getString(R.string.recordingStarted))
    }

    private fun CreatFileName() {
        val timestamp = VoiceRecord().CreateName()
        rawFile = File(requireContext().getExternalFilesDir(null), "recorded_audio_$timestamp.raw")
        wavFile = File(requireContext().getExternalFilesDir(null), "recorded_audio_$timestamp.wav")
    }

    private fun StartAnimationMicrophon() {
        val moveAnimation: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.move_animation)
        bindingAddMemory.AppCompatImageViewRecord.startAnimation(moveAnimation)
    }

    private fun StartTimer(textView: TextView) {
        handlerChornimeter.post(object : Runnable {
            override fun run() {
                val elapsedMillis = System.currentTimeMillis() - startTime

                val minutes = (elapsedMillis / 1000) / 60
                val seconds = (elapsedMillis / 1000) % 60
                val milliseconds = (elapsedMillis % 1000) / 10 // تبدیل میلی‌ثانیه به صدم‌ثانیه

                val time = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
                textView.text = time

                handlerChornimeter.postDelayed(this, 10) // به‌روزرسانی هر 10 میلی‌ثانیه
            }
        })
    }



    private fun RunSoundWaveAnimation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        audioRecord.startRecording()

        recordingThread = Thread {
            while (isRecording) {
                val read = audioRecord.read(buffer, 0, bufferSize)
                if (read > 0) {
                    // Update UI
                    val amplitude = CalculateAmplitude(buffer)
                    mainHandler.post {
                        UpdateSoundWave(amplitude) // Update sound animation
                    }
                }
                //Save data in WAV
                //writeAudioDataToRawFile(rawFile)
                writeAudioDataAsync(buffer.copyOf(read))
            }
        }
        recordingThread.start()
    }

    private fun writeAudioDataToRawFile(file: File) {
        val data = ByteArray(bufferSize)
        FileOutputStream(file).use { outputStream ->
            while (isRecording) {
                val read = audioRecord.read(data, 0, data.size)
                if (read > 0) {
                    outputStream.write(data, 0, read)
                }
            }
        }
    }

    private fun writeAudioDataAsync(data: ByteArray) {
        Thread {
            FileOutputStream(rawFile, true).use { outputStream ->
                outputStream.write(buffer)
            }
        }.start()
    }





    private fun convertRawToWav(rawFile: File, wavFile: File) {
        val rawData = rawFile.readBytes()
        FileOutputStream(wavFile).use { outputStream ->
            writeWavHeader(
                outputStream,
                rawData.size.toLong(),
                44100,
                1,
                16
            )
            outputStream.write(rawData)
        }
        rawFile.delete()
    }

    private fun writeWavHeader(
        outputStream: FileOutputStream,
        totalAudioLen: Long,
        sampleRate: Int,
        channels: Int,
        bitsPerSample: Int
    ) {
        val totalDataLen = totalAudioLen + 36
        val byteRate = sampleRate * channels * bitsPerSample / 8

        val header = ByteArray(44)

        // فرمت WAV
        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()

        // طول داده
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = ((totalDataLen shr 8) and 0xff).toByte()
        header[6] = ((totalDataLen shr 16) and 0xff).toByte()
        header[7] = ((totalDataLen shr 24) and 0xff).toByte()

        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()

        // Subchunk1
        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()

        header[16] = 16
        header[17] = 0
        header[18] = 0
        header[19] = 0

        header[20] = 1
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0

        header[24] = (sampleRate and 0xff).toByte()
        header[25] = ((sampleRate shr 8) and 0xff).toByte()
        header[26] = ((sampleRate shr 16) and 0xff).toByte()
        header[27] = ((sampleRate shr 24) and 0xff).toByte()

        header[28] = (byteRate and 0xff).toByte()
        header[29] = ((byteRate shr 8) and 0xff).toByte()
        header[30] = ((byteRate shr 16) and 0xff).toByte()
        header[31] = ((byteRate shr 24) and 0xff).toByte()

        header[32] = (channels * bitsPerSample / 8).toByte()
        header[33] = 0

        header[34] = bitsPerSample.toByte()
        header[35] = 0

        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()

        header[40] = (totalAudioLen and 0xff).toByte()
        header[41] = ((totalAudioLen shr 8) and 0xff).toByte()
        header[42] = ((totalAudioLen shr 16) and 0xff).toByte()
        header[43] = ((totalAudioLen shr 24) and 0xff).toByte()

        outputStream.write(header, 0, 44)
        voicePath = wavFile.path
    }





    private fun CalculateAmplitude(buffer: ByteArray): Int {
        var sum = 0.0
        for (i in buffer.indices step 2) {
            val sample = (buffer[i].toInt() or (buffer[i + 1].toInt() shl 8)).toShort()
            sum += sample * sample
        }
        return Math.sqrt(sum / (buffer.size / 2.0)).toInt()
    }
    private fun UpdateSoundWave(amplitude: Int) {
        handler.post {
            println("Amplitude: $amplitude")
            /*binding.AppCompatImageViewSoundWaveView.layoutParams.height = amplitude +200 // /2
            binding.AppCompatImageViewSoundWaveView.requestLayout()*/


            bindingAddMemory.viewForWavSound.Viwe1.layoutParams.height =  amplitude + 20
            bindingAddMemory.viewForWavSound.Viwe1.requestLayout()
            bindingAddMemory.viewForWavSound.Viwe2.layoutParams.height = amplitude + 40
            bindingAddMemory.viewForWavSound.Viwe2.requestLayout()
            bindingAddMemory.viewForWavSound.Viwe3.layoutParams.height = amplitude + 80
            bindingAddMemory.viewForWavSound.Viwe3.requestLayout()
            bindingAddMemory.viewForWavSound.Viwe4.layoutParams.height = amplitude + 50
            bindingAddMemory.viewForWavSound.Viwe4.requestLayout()
            bindingAddMemory.viewForWavSound.Viwe5.layoutParams.height = amplitude + 20
            bindingAddMemory.viewForWavSound.Viwe5.requestLayout()
            bindingAddMemory.viewForWavSound.Viwe6.layoutParams.height = amplitude + 40
            bindingAddMemory.viewForWavSound.Viwe6.requestLayout()
            bindingAddMemory.viewForWavSound.Viwe7.layoutParams.height = amplitude + 50
            bindingAddMemory.viewForWavSound.Viwe7.requestLayout()
        }
    }






    private fun StartPlayVoice(){
        if( !voicePath.isNullOrEmpty() && !isRecording && !mediaPlayer.isPlaying){
            runVoice_1(wavFile.path)
        }
    }

    private fun runVoice_1(filePath:String) {
        try {
            mediaPlayer.run {
                setDataSource(filePath)
                prepare()
                start()
            }

            mediaPlayer.setOnCompletionListener {
                it.release()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()  // در صورت بروز خطا
        }
        catch (e: NullPointerException) {
            e.printStackTrace()  // در صورت بروز خطا
        }
    }

    private fun StopRecordingVoice() {
        if(isRecording){
            elapsedTime = System.currentTimeMillis() - startTime
            bindingAddMemory.Chronometer.stop()
            handlerChornimeter.removeCallbacksAndMessages(null) // توقف تایمر

            bindingAddMemory.AppCompatImageViewRecord.clearAnimation()
            SoundRecord().StopRecord()
            isRecording = false

            audioRecord.stop()
            audioRecord.release()
            recordingThread.interrupt()

            convertRawToWav(rawFile, wavFile)
            GlobalFunction().ShowToast(requireContext() , getString(R.string.voiceSave))
        }
    }

    /*private fun PauseRecrdingVoice() {
        SetPauseBtnVisible(true)
        if(isRecording && !isPause){
            StopAnimation()
            //SoundRecord().PauseRecord()
            isPause = true
        }
    }*/

    private fun StopAnimation() {
        isRecording = false
        elapsedTime = System.currentTimeMillis() - startTime
        bindingAddMemory.Chronometer.stop()
        handlerChornimeter.removeCallbacksAndMessages(null) // توقف تایمر

        bindingAddMemory.AppCompatImageViewRecord.clearAnimation()
    }

    /*private fun SetPauseBtnVisible(show : Boolean){
        if (show){
            binding.AppCompatImageViewPause.visibility = View.VISIBLE
        }
        else{
            binding.AppCompatImageViewPause.visibility = View.GONE
        }
    }*/

    //********* Request permission***********
    private fun requestPermissions_RECORD_AUDIO() {
        val permissions = mutableListOf(Manifest.permission.RECORD_AUDIO )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        requestPermissionLauncher.launch(permissions.toTypedArray())
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.all { it.value }
            if (granted) {
                Toast.makeText(requireContext(), "Permissions granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permissions denied!", Toast.LENGTH_SHORT).show()
            }
        }
    //********* Request permission***********


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(MainActivity.memoryInfoList != null ){
            if (MainActivity.memoryInfoList!!.size > 0){
                //val selectedPosition = bindingAddMemory.SpinnerMemoryList?.selectedItemPosition ?: -1
                //outState.putInt("SPINNER_POSITION", selectedPosition)
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            val savedPosition = it.getInt("SPINNER_POSITION", -1)
            if (savedPosition != -1) {
                bindingAddMemory.SpinnerMemoryList?.setSelection(savedPosition)
            }
        }
    }






}//end fragment