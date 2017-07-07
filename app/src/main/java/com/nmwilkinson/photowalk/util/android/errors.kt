package com.nmwilkinson.photowalk.util.android

import android.app.Activity
import android.app.AlertDialog
import com.nmwilkinson.photowalk.R

fun errorDialog(activity: Activity, msg: String) {
    return AlertDialog.Builder(activity, R.style.dialog_style)
            .setTitle(msg)
            .setPositiveButton("OK", { _, _ -> })
            .create()
            .show()
}