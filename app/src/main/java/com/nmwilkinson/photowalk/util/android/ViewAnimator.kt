package com.nmwilkinson.photowalk.util.android

import android.view.View
import android.widget.ViewAnimator

fun ViewAnimator.setDisplayedChild(child: View) {
    displayedChild = indexOfChild(child)
}