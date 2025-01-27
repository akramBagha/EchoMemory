package com.bagha.echomemory.B_Function

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bagha.echomemory.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.Exception

class B_AlertDialogManager : Activity() {

    interface ActionAfterAlert {
        fun isTrue(isTrue: Boolean)
        fun selectItem(position: Int)
    }

    companion object {
        var buttonOkBtn: Button? = null
        var buttonCancelBtn: Button? = null

        //__________ Alert showAlertMessage
        public fun showAlertMessage(
            activity: Activity?,
            title: String,
            message: String,
            btnText: String
        ) {
            try {
                CustomMaterialAlertDialog(activity , title, message, btnText, null, true)
            } catch (e: Exception) {
                Log.e("error_SweetAlertDialog", e.message!!)
            }
        }


        //__________ Alert showAlertMessage_isTrue
        fun showAlertMessage_isTrue(
            activity: Activity?,
            message: String,
            actionAfterAlert: ActionAfterAlert
        ) {
            try {
                val alertDialog = CustomMaterialAlertDialog(
                    activity,
                    activity!!.getString(R.string.dearUser ),
                    message,
                    activity.getString( R.string.yes ),
                    activity.getString( R.string.no ),
                    false
                )
                buttonOkBtn!!.setOnClickListener(View.OnClickListener {
                    actionAfterAlert.isTrue(true)
                    alertDialog.dismiss()
                })
                buttonCancelBtn!!.setOnClickListener(View.OnClickListener {
                    actionAfterAlert.isTrue(false)
                    alertDialog.dismiss()
                })
            } catch (e: Exception) {
                Log.e("error_SweetAlertDialog", e.message!!)
            }
        }

        /*//_____________ showAlertMessage_selectImageFromCameraOrGallery
        fun showAlertMessage_selectImageFromCameraOrGallery(
            activity: Activity,
            message: String?,
            actionAfterAlert: ActionAfterAlert
        ) {
            val options: Array<String> =
                activity.getResources().getStringArray(R.array.typeTakeImageList)
            val singelItelSelected = intArrayOf(0)
            try {
                val customAlertDialogView: View = LayoutInflater.from(activity)
                    .inflate(R.layout.custom_material_dialobox, null, false)
                val dialogIcone: AppCompatImageView =
                    customAlertDialogView.findViewById<AppCompatImageView>(R.id.dialogIcone)
                val dialogTitle: TextView =
                    customAlertDialogView.findViewById<TextView>(R.id.dialogTitle)
                val dialogMessage: TextView =
                    customAlertDialogView.findViewById<TextView>(R.id.dialogMessage)
                val buttonOkBtn: Button =
                    customAlertDialogView.findViewById<Button>(R.id.buttonOkBtn)
                val CardViewCancelBtn: CardView =
                    customAlertDialogView.findViewById<CardView>(R.id.CardViewCancelBtn)
                val TextView_dialogCancelBtn_: TextView =
                    customAlertDialogView.findViewById<TextView>(R.id.TextView_dialogCancleBtn)

                TextView_dialogAcceptBtn = TextView_dialogAcceptBtn_
                TextView_dialogCancleBtn = TextView_dialogCancelBtn_
                CardViewCancelBtn.setVisibility(View.VISIBLE)
                dialogIcone.visibility = View.GONE

                val select = StringOnLine().getString(activity , R.string.iSelected , "iSelected")
                val cancel = StringOnLine().getString(activity , R.string.cancel , "cancel")

                TextView_dialogCancelBtn_.text = cancel
                TextView_dialogAcceptBtn_.text = select

                val materialAlertDialogBuilder = MaterialAlertDialogBuilder(activity)
                materialAlertDialogBuilder.setView(customAlertDialogView)
                materialAlertDialogBuilder.setCancelable(false)
                materialAlertDialogBuilder.setSingleChoiceItems(options, 0,
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, item: Int) {
                            singelItelSelected[0] = item
                        }
                    })
                val alertDialog: AlertDialog = materialAlertDialogBuilder.create()
                alertDialog.show()

                TextView_dialogAcceptBtn!!.setOnClickListener(View.OnClickListener {
                    actionAfterAlert.selectItem(singelItelSelected[0])
                    alertDialog.dismiss()
                })

                CardViewCancelBtn.setOnClickListener(View.OnClickListener { alertDialog.dismiss() })
                //////////////////////////////

            } catch (e: Exception) {
                Log.e("error_SweetAlertDialog", e.message!!)
            }
        }*/

        //_____________ showAlertMessage_selectItems
        fun showAlertMessage_selectItems(
            activity: Activity,
            message: String?,
            options: Array<String>,
            actionAfterAlert: ActionAfterAlert
        ) {
            val singelItelSelected = intArrayOf(0)
            try {
                val customAlertDialogView: View = LayoutInflater.from(activity)
                    .inflate(R.layout.custom_material_dialobox, null, false)
                val dialogMessage: TextView =
                    customAlertDialogView.findViewById<TextView>(R.id.dialogMessage)


                val buttonCancelBtn: Button =
                    customAlertDialogView.findViewById<Button>(R.id.buttonCancelBtn)
                val buttonOkBtn: Button =
                    customAlertDialogView.findViewById<Button>(R.id.buttonOkBtn)

                dialogMessage.text = message
                buttonCancelBtn.setVisibility(View.VISIBLE)

                buttonOkBtn.text = activity.getString(R.string.iChoose)
                buttonCancelBtn.text = activity.getString(R.string.cancel)

                val materialAlertDialogBuilder = MaterialAlertDialogBuilder(activity)
                materialAlertDialogBuilder.setView(customAlertDialogView)
                materialAlertDialogBuilder.setCancelable(false)
                materialAlertDialogBuilder.setSingleChoiceItems(options, 0,
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, item: Int) {
                            if(singelItelSelected != null && singelItelSelected.size > 0){
                                singelItelSelected[0] = item
                            }
                        }
                    })
                val alertDialog: AlertDialog = materialAlertDialogBuilder.create()
                alertDialog.show()

                buttonOkBtn!!.setOnClickListener(View.OnClickListener {
                    if(singelItelSelected != null && singelItelSelected.size > 0){
                        actionAfterAlert.selectItem(singelItelSelected[0])
                        alertDialog.dismiss()
                    }
                })

                buttonCancelBtn.setOnClickListener(View.OnClickListener {
                    alertDialog.dismiss()
                })
                //////////////////////////////

            } catch (e: Exception) {
                Log.e("error_SweetAlertDialog", e.message!!)
            }
        }


        //__________ customMaterialAlert
        @SuppressLint("MissingInflatedId")
        private fun CustomMaterialAlertDialog(
            activity: Activity?,
            title: String,
            message: String,
            btnAccept: String,
            btnCancel: String?,
            enableAcceptBtn: Boolean
        ): AlertDialog {
            val customAlertDialogView: View = LayoutInflater.from(activity)
                .inflate(R.layout.custom_material_dialobox, null, false)
            val dialogTitle: TextView =
                customAlertDialogView.findViewById<TextView>(R.id.dialogTitle)
            val dialogMessage: TextView =
                customAlertDialogView.findViewById<TextView>(R.id.dialogMessage)


           val buttonCancelBtn: Button =
                customAlertDialogView.findViewById<Button>(R.id.buttonCancelBtn)
            val buttonOkBtn: Button =
                customAlertDialogView.findViewById<Button>(R.id.buttonOkBtn)


            Companion.buttonOkBtn = buttonOkBtn
            Companion.buttonCancelBtn = buttonCancelBtn

            if (!btnCancel.isNullOrEmpty()) {
                buttonCancelBtn!!.text= btnCancel
                buttonCancelBtn!!.setVisibility(View.VISIBLE)
            }
            dialogTitle.text =(title)
            dialogMessage.text = (message)
            buttonOkBtn!!.text = (btnAccept)

            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(activity!!)
            materialAlertDialogBuilder.setView(customAlertDialogView)
            materialAlertDialogBuilder.setCancelable(false)
            val alertDialog: AlertDialog = materialAlertDialogBuilder.create()
            alertDialog.show()
            if (enableAcceptBtn) {
                buttonOkBtn!!.setOnClickListener(View.OnClickListener {
                    alertDialog.dismiss()
                })
            }
            return alertDialog
        }
        //__________ customMaterialAlert

    }
} //end class
