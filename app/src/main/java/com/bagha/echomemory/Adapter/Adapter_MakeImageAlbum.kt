package com.bagha.echomemory.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bagha.echomemory.B_Function.B_SetImageView
import com.bagha.echomemory.R
import java.util.*

class Adapter_MakeImageAlbum :
    RecyclerView.Adapter<Adapter_MakeImageAlbum.ViewHolder>{


    var arrayList: List<String> = ArrayList<String>()
    var activity: Activity? = null

    constructor(activity: Activity , list: List<String>){
        this.arrayList = list
        this.activity = activity
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int ): ViewHolder {
        val layout: Int = R.layout.item_for_make_image_album
        val v = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            //var imageBitmap = BitmapFactory.decodeFile(arrayList[position])
            //viewHolder.imageViewForMakeAlbum.setImageBitmap(imageBitmap)

            B_SetImageView().SetImageByPathString(activity!! , arrayList[position] , viewHolder.AppCompatImageViewForMakeAlbum)

        }//end try
        catch (e : NullPointerException){
            e.printStackTrace()
        }
        catch (e : Exception){
            e.printStackTrace()
        }

    }//end main

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val AppCompatImageViewForMakeAlbum: AppCompatImageView = itemView.findViewById<AppCompatImageView>(R.id.AppCompatImageViewForMakeAlbum)
        }




}//end class