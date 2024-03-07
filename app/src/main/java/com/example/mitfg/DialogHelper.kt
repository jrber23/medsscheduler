package com.example.mitfg

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class DialogHelper(private val context: Context) {

    fun showPopUp(message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(R.string.accept, DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            })
        val alert = builder.create()
        alert.show()
    }

}