package com.bagha.echomemory.ui.PlayMemory

import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.os.Bundle
import android.os.DeadObjectException
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bagha.echomemory.B_Function.GifImageCall
import com.bagha.echomemory.DB_Room.Model_Table.Image_Table
import com.bagha.echomemory.DB_Room.Model_Table.MemoInfo_Table
import com.bagha.echomemory.MainActivity
import com.bagha.echomemory.R
import com.bagha.echomemory.Slaider.Slider
import com.bagha.echomemory.Slaider.Slider_Items
import com.bagha.echomemory.databinding.FragmentSlideshowBinding
import java.util.concurrent.TimeUnit

class PalyMemoryFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    val mediaPlayer = MediaPlayer()

    private val binding get() = _binding!!

    private val _paly = "play"
    private val _pause = "pause"

    var filePath_ =""
    var arrayLIstImageAlbum :List<Image_Table> = ArrayList()
    lateinit var titleMemoryVoice : MemoInfo_Table

    //***********Player
    private var currentTime = 0.0
    private var finalTime = 0.0
    private val myHandler: Handler = Handler(Looper.getMainLooper())
    private val forwardTime = 3000
    private val backwardTime = 3000
    //private lateinit var mediaPlayer: MediaPlayer
    private var oneTimeOnly = 0
    private var isStop = false
    //***********

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

         _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root


        try {
            if(MainActivity.memoryTitleId != -1){
                titleMemoryVoice = MainActivity.DB.getMemoryTitleById(MainActivity.memoryTitleId)
                arrayLIstImageAlbum = MainActivity.DB.getImageListById(MainActivity.memoryTitleId)
                filePath_ = MainActivity.DB.getSoundById(MainActivity.memoryTitleId)?.addressSound ?: ""

                //filePath_ = MainActivity.DB.getSoundById(MainActivity.memoryTitleId).addressSound

                if( !filePath_.isNullOrEmpty()){
                    if(!titleMemoryVoice.description.isNullOrEmpty()){
                        binding.CardViewDescription.visibility = View.VISIBLE
                        binding.TextViewDescription.text = titleMemoryVoice.description
                    }

                    //__________
                    runVoice(filePath_)
                    mediaPlayer.isLooping = true
                    /*mediaPlayer.setOnCompletionListener {
                        println("endMediaPlayer => yes")
                        //it.release()
                        mediaPlayer.reset()
                        runVoice(filePath_)
                        isStop = true
                    }*/
                    //__________________

                    //__________Slider________
                    SetSlider()
                    //__________Slider________
                }
            }


        }
        catch (e : IllegalStateException){
            e.printStackTrace()
        }
        catch (e : DeadObjectException){
            e.printStackTrace()
        }
        catch (e : NullPointerException){
            e.printStackTrace()
        }
        catch (e : Exception){
            e.printStackTrace()
        }






        /*val textView: TextView = binding.textSlideshow
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    private fun runVoice(filePath: String) {

        val gif = GifImageCall(requireContext())
        gif.ShowGif_Drawable(binding.ImageViewGifWav , R.drawable.sound_anim_2)

        binding.SeekBarVoice.setClickable(false)

        mediaPlayer.run {
            setDataSource(filePath)
            prepare()
            start()
        }
        RunSeekBar()
        setIconColor(_paly)
        isStop = false




        binding.ImageViewPaly.setOnClickListener(View.OnClickListener {
            if( !mediaPlayer.isPlaying && !isStop){ // this pause
                mediaPlayer.start()
                RunSeekBar()
                setIconColor(_paly)
            }
            else if(isStop){
                runVoice(filePath_)
            }
        })

        binding.ImageViewPause.setOnClickListener(View.OnClickListener {
            mediaPlayer.pause();
            setIconColor(_pause)
        })

        binding.ImageViewForward.setOnClickListener(View.OnClickListener {
            val temp = currentTime

            if ((temp + forwardTime) <= finalTime) {
                currentTime = currentTime + forwardTime;
                mediaPlayer.seekTo(currentTime.toInt())
            }
        })
        binding.ImageViewRewindBack.setOnClickListener(View.OnClickListener {
            val temp = currentTime;

            if((temp-backwardTime)>0){
                currentTime = currentTime - backwardTime;
                mediaPlayer.seekTo(currentTime.toInt());
            }
        })
    }

    private fun setIconColor(type: String) {
        if(type == _pause){
            binding.ImageViewPause.isEnabled = (false);
            binding.ImageViewPaly.isEnabled = (true)
            binding.ImageViewPause.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grayLight))
            binding.ImageViewPaly.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray))
            //GifImageCall(requireContext()).ShowFirstFram(null , R.drawable.sound_anim_2)
        }
        else if(type == _paly){
            binding.ImageViewPaly.isEnabled = (false)
            binding.ImageViewPause.isEnabled = (true)
            binding.ImageViewPaly.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grayLight))
            binding.ImageViewPause.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray))
            //GifImageCall(requireContext()).ShowGif_Drawable(binding.ImageViewGifWav , R.drawable.sound_anim_2)
        }
    }

    private fun RunSeekBar() {
        try {
            finalTime = mediaPlayer.duration.toDouble()
            currentTime = mediaPlayer.currentPosition.toDouble()

            if (oneTimeOnly == 0) {
                binding.SeekBarVoice.setMax(finalTime.toInt())
                oneTimeOnly = 1
            }

            binding.SeekBarVoice.setProgress(currentTime.toInt())
            myHandler.postDelayed(UpdateSongTime, 100)
        }

        catch (e : IllegalStateException){
            e.printStackTrace()
        }
        catch (e : DeadObjectException){
            e.printStackTrace()
        }
        catch (e : NullPointerException){
            e.printStackTrace()
        }
        catch (e : Exception){
            e.printStackTrace()
        }


    }

    private fun SetTextViewTime() {
        /*binding.TextViewTimer.text =
            kotlin.String.format(
                "0%d : 0%d ",
                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) -
                        TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(
                                startTime.toLong()
                            )
                        )
            )*/
        var hou=TimeUnit.MILLISECONDS.toHours((currentTime.toLong()))
        var min=TimeUnit.MILLISECONDS.toMinutes((currentTime.toLong()))
        var sec=TimeUnit.MILLISECONDS.toSeconds((currentTime.toLong()))
        var houSTR = ""
        var minSTR = ""
        var secSTR = ""

        if(hou <= 9){
            houSTR = "0" + hou
        }
        else{
            houSTR = hou.toString()
        }

        if(min <= 9){
            minSTR = "0" + min
        }
        else{
            minSTR = min.toString()
        }

        if(sec <= 9){
            secSTR = "0" + sec
        }
        else{
            secSTR = sec.toString()
        }

        binding.TextViewTimer.text = houSTR + ":" + minSTR +":" + secSTR
    }

    private val UpdateSongTime: Runnable = object : Runnable {

        override fun run() {
            if(mediaPlayer.isPlaying == true){
                currentTime = mediaPlayer.currentPosition.toDouble()
                SetTextViewTime()

                binding.SeekBarVoice.setProgress(currentTime.toInt())
                myHandler.postDelayed(this, 100)

                binding.ImageViewGifWav.visibility = View.VISIBLE
            }
            else{
                binding.ImageViewGifWav.visibility = View.INVISIBLE
                if(mediaPlayer.currentPosition == mediaPlayer.duration){
                    setIconColor(_paly)
                }
                else{
                    setIconColor(_pause)
                }
            }
            /*currentTime = mediaPlayer.currentPosition.toDouble()
            SetTextViewTime()

            binding.SeekBarVoice.setProgress(currentTime.toInt())
            myHandler.postDelayed(this, 100)
            if(mediaPlayer.currentPosition == mediaPlayer.duration){
                binding.ImageViewGifWav.visibility = View.INVISIBLE
                setIconColor(_play)
            }
            else{
                binding.ImageViewGifWav.visibility = View.VISIBLE
            }*/
        }

    }


    private fun SetSlider() {
        var arrayList_sliderImage = ArrayList<Slider_Items>()
        for (i in 0 .. arrayLIstImageAlbum.size-1){
            var s = Slider_Items()
            s.image_deviceAddress = arrayLIstImageAlbum[i].addressImage
            arrayList_sliderImage.add(s)
        }
        binding!!.slider.RelativeLayoutSlider.visibility = View.VISIBLE
        val slider = Slider(
            requireActivity(),
            arrayList_sliderImage,
            binding!!.slider.ViewPagerSliderHome,
            binding!!.slider.LinearLayoutCircleBtn
        )
        slider.RunSlider()
    }








    override fun onStop() {
        super.onStop()
        /*if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }*/
        if(mediaPlayer.isPlaying == true){
            mediaPlayer.release()
        }
        else{
            mediaPlayer.setOnCompletionListener {
                it.release()
            }
        }
        myHandler.removeCallbacksAndMessages(null)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
