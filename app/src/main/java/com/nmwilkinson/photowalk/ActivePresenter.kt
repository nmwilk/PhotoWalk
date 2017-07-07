package com.nmwilkinson.photowalk

import android.util.Log

class ActivePresenter(private val model: ActiveModel) : ActiveContract.Presenter {
    private var view: ActiveContract.View? = null

    override fun setView(view: ActiveContract.View?) {
        this.view = view
        if (model.active()) {
            view?.startLocationTracking()
        }
        view?.updateUI(model.state())
    }

    override fun start() {
        model.startTracking()
        view?.startLocationTracking()
        view?.updateUI(model.state())
    }

    override fun stop() {
        model.stopTracking()
        view?.stopLocationTracking()
        view?.updateUI(model.state())
    }

    override fun newImage(url: String) {
        Log.d(TAG, "newImage $url")
        model.addImage(url)
        view?.updateUI(model.state())
    }

    val TAG: String = ActivePresenter::class.java.simpleName
}

