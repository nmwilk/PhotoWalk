package com.nmwilkinson.photowalk

class ActiveModel(private var active: Boolean, imageUrls: List<String>) {

    private val imageUrls = ArrayList<String>(imageUrls)

    fun active() = active

    fun startTracking() {
        imageUrls.clear()
        active = true
    }

    fun stopTracking() {
        active = false
    }

    fun addImage(url: String) {
        if (!imageUrls.contains(url)) imageUrls.add(url)
    }

    fun state() = ActiveContract.UIState(active, imageUrls)
}