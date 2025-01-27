package com.bagha.echomemory.B_Function

import android.content.Context
import android.view.View
import android.widget.ImageView
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class GifImageCall (context: Context ) {
    lateinit var context : Context
    lateinit var imageLoader: ImageLoader


    init {
        this.context = context
        ImageLoderCall()
    }

    fun ImageLoderCall(){
        imageLoader = ImageLoader.Builder(context)
            .components {
                add(GifDecoder.Factory())
            }
            .build()
    }

    fun ShowGif_Drawable(imageView : ImageView, drawableImage : Int ){
        val request = ImageRequest.Builder(context)
            .data(drawableImage)
            .target(imageView)
            .build()

        imageLoader.enqueue(request)
    }


    fun ShowGif_AssetFile(imageView : ImageView, assetImage : String ){
        //assetImage = "file:///android_asset/yourfile.gif"
        val request = ImageRequest.Builder(context)
            .data(assetImage)
            .target(imageView)
            .build()

        imageLoader.enqueue(request)
    }


    fun ShowGif_Url(imageView : ImageView, url : String ){
        //val gifUrl = "https://media.giphy.com/media/3o7TKsQvIoT2mNHz8k/giphy.gif"
        val request = ImageRequest.Builder(context)
            .data(url)
            .target(imageView)
            .build()

        imageLoader.enqueue(request)
    }

    fun ShowFirstFram(urlOrAsset : String?, drawableImage : Int?){
        var request : ImageRequest
        if(urlOrAsset != null){
            request = ImageRequest.Builder(context)
                .data(urlOrAsset)
                .allowHardware(false)
                .build()
            imageLoader.enqueue(request)
        }
        else if(drawableImage != null){
            request = ImageRequest.Builder(context)
                .data(drawableImage)
                .allowHardware(false)
                .build()
            imageLoader.enqueue(request)
        }
    }

    fun ShowYOYO(view : View){
        if(view != null){
            YoYo.with(Techniques.Tada).duration(10000).repeat(50)
                .playOn(view)
        }
    }
    fun ShowYOYOCustom(view : View){
        if(view != null){
            YoYo.with(Techniques.Bounce).duration(1000).repeat(7)
                .playOn(view)
        }
    }
}//end class