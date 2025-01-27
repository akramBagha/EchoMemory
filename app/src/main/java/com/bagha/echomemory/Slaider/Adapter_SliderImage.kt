package com.bagha.echomemory.Slaider

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bagha.echomemory.B_Function.B_SetImageView

class Adapter_SliderImage(var activity: Activity, arrayList_urlImage: ArrayList<Slider_Items> ) :
    PagerAdapter() {
    var arrayList_urlImage = ArrayList<Slider_Items>()

    init {
        this.arrayList_urlImage = arrayList_urlImage
    }

    override fun getCount(): Int {
        return arrayList_urlImage.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj as ImageView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mImageView = AppCompatImageView(activity)
        mImageView.scaleType = ImageView.ScaleType.FIT_XY
        B_SetImageView().SetImageByPathString(activity , arrayList_urlImage[position].image_deviceAddress.toString(), mImageView)


        (container as ViewPager).addView(mImageView, 0)
        return mImageView
    }

    override fun destroyItem(container: ViewGroup, i: Int, obj: Any) {
        (container as ViewPager).removeView(obj as ImageView)
    }
}