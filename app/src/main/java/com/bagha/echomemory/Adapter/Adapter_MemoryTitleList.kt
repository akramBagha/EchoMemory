package com.bagha.echomemory.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bagha.echomemory.B_Function.B_SetImageView
import com.bagha.echomemory.DB_Room.Model_Table.MemoInfo_Table
import com.bagha.echomemory.R
import java.util.*

class Adapter_MemoryTitleList :
    RecyclerView.Adapter<Adapter_MemoryTitleList.ViewHolder>{


    var arrayList: List<MemoInfo_Table> = ArrayList<MemoInfo_Table>()
    var activity: Activity? = null
    var actionOnClick : ActionOnClick? = null

    constructor(activity: Activity , list: List<MemoInfo_Table> , actionOnClick: ActionOnClick){
        this.arrayList = list
        this.activity = activity
        this.actionOnClick = actionOnClick
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int ): ViewHolder {
        val layout: Int = R.layout.item_transform
        val v = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            viewHolder.TextViewTitle.text = (arrayList[position].title)

            B_SetImageView().SetImageByPathString(activity!! , arrayList[position].imageCover , viewHolder.AppCompatImageViewCover)

            viewHolder.itemView.setOnClickListener(View.OnClickListener {
                actionOnClick!!.playMemory(position)
            })

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
        val AppCompatImageViewCover: AppCompatImageView = itemView.findViewById<AppCompatImageView>(R.id.AppCompatImageViewCover)
        val TextViewTitle: TextView = itemView.findViewById<TextView>(R.id.TextViewTitle)
        }



    interface ActionOnClick {
        fun playMemory(position: Int)
    }

}//end class