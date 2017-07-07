package com.nmwilkinson.photowalk

interface ActiveContract {
    interface Presenter {
        fun setView(view: View?)
        fun start()
        fun stop()
        fun newImage(url: String)
    }

    interface View {
        fun startLocationTracking()
        fun stopLocationTracking()
        fun updateUI(state: UIState)
    }

    data class UIState(val active: Boolean, val imageUrls: List<String>)
}