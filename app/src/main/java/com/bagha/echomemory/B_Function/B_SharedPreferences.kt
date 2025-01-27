package com.bagha.echomemory.B_Function

import android.content.Context
import android.content.SharedPreferences
import com.bagha.echomemory.R
import java.lang.NullPointerException

class B_SharedPreferences {


    var context : Context? = null

    companion object{
        val perferance_label_firstOpenApp = "firstOpenApp"
        var preferences : SharedPreferences ? = null
    }



    constructor(context: Context ) {
        try {
            if(context != null){
                this.context = context
                preferences = context.getSharedPreferences(context.getString(R.string.kotlinsharedpreference),
                    Context.MODE_PRIVATE)

            }
        }
        catch (e: NullPointerException){
            e.printStackTrace()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    /*constructor(context: Context , user : User) {
        try {
            if(context != null){
                this.context = context

                //mobile = user.mobile
                access_token = user.access_token
                refresh_token = user.refresh_token

                preferences = context.getSharedPreferences(context.getString(R.string.kotlinsharedpreference),
                    Context.MODE_PRIVATE)
                preferences!!.edit()
                    .putString(perferance_label_access_token, access_token).commit()
                preferences!!.edit()
                    .putString(perferance_label_refresh_token, refresh_token).commit()

                UserInformation(user)
            }
        }
        catch (e: NullPointerException){
            e.printStackTrace()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }*/










    fun removeShairPer() {
        if (context != null) {
            preferences = context!!.getSharedPreferences(context!!.getString(R.string.kotlinsharedpreference),  Context.MODE_PRIVATE)
        }

        preferences!!.edit().remove(perferance_label_firstOpenApp)
            .apply()
        preferences!!
    }


}