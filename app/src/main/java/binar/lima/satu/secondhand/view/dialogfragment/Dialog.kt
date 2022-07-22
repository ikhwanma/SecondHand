package binar.lima.satu.secondhand.view.dialogfragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import binar.lima.satu.secondhand.R

class Dialog(val activity: Activity) {

    private lateinit var dialog : AlertDialog

    @SuppressLint("InflateParams")
    fun startDialog(){
        val builder = AlertDialog.Builder(activity)

        val inflater = activity.layoutInflater

        builder.setView(inflater.inflate(R.layout.dialog_loading, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}