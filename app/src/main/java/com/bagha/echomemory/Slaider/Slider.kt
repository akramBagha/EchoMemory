package com.bagha.echomemory.Slaider

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bagha.echomemory.R
import java.util.Timer
import java.util.TimerTask


class Slider(
    context: Activity,
    arrayList_urlImage: ArrayList<Slider_Items>,
    viewPager_slider_: ViewPager,
    LinearLayoutCircleBtn: LinearLayout
) {
    var arrayList_urlImage = ArrayList<Slider_Items>()
    var arrayList_circleBtn = ArrayList<View>()
    var adapterView: Adapter_SliderImage? = null
    var ViewPager_slider: ViewPager
    var LinearLayoutCircleBtn: LinearLayout
    var context: Activity

    private var mTimer: Timer? = null


    init {
        this.arrayList_urlImage = arrayList_urlImage
        ViewPager_slider = viewPager_slider_
        this.context = context
        this.LinearLayoutCircleBtn = LinearLayoutCircleBtn
    }

    fun RunSlider() {
        adapterView = Adapter_SliderImage(context, arrayList_urlImage)
        ViewPager_slider.adapter = adapterView
        arrayList_circleBtn.clear()
        val param = LinearLayout.LayoutParams(
            context.resources.getDimension(R.dimen.app_slider_bottom_button).toInt(),
            context.resources.getDimension(R.dimen.app_slider_bottom_button).toInt()
        )
        for (i in arrayList_urlImage.indices) {
            val AppCompatImageView_circle = View(context)
            arrayList_circleBtn.add(AppCompatImageView_circle)
            param.leftMargin = 5
            param.rightMargin = 5
            AppCompatImageView_circle.layoutParams = param
            LinearLayoutCircleBtn.addView(AppCompatImageView_circle)
        }
        ViewPager_slider.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                setCircleButton(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        RunTimer_2()

    } //end RunSlider

    private fun RunTimer_2() {
        mTimer = Timer()
        mTimer!!.schedule(TimeDisplayTimerTask() ,5,5000)
    }




    private fun setCircleButton(position: Int) {
        for (i in arrayList_circleBtn.indices) {
            arrayList_circleBtn[i].setBackgroundResource(
                if (i == position) R.drawable.record else R.drawable.stop
            )
        }
    }


    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private inner class  TimeDisplayTimerTask : TimerTask() {
        var current = ViewPager_slider.currentItem
        override fun run() {
            mHandler.post(Runnable {
                if (current < arrayList_urlImage.size) {
                    /*YoYo.with(Techniques.Bounce).duration(1000).repeat(0)
                        .playOn(ViewPager_sliderHome)*/
                    ViewPager_slider.currentItem = current
                    current += 1
                } else {
                    current = 0
                    ViewPager_slider.currentItem = current
                }
            })
        }
    }
} //end class
