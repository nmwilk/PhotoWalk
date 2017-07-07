package com.nmwilkinson.photowalk

import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.jakewharton.rxbinding2.view.clicks
import com.nmwilkinson.photowalk.flickr.Flickr
import com.nmwilkinson.photowalk.flickr.FlickrPhoto
import com.nmwilkinson.photowalk.flickr.FlickrSearchService
import com.nmwilkinson.photowalk.location.isLocationGranted
import com.nmwilkinson.photowalk.location.requestLocationPermission
import com.nmwilkinson.photowalk.util.android.SharedPreferencesHelper
import com.nmwilkinson.photowalk.util.android.errorDialog
import com.nmwilkinson.photowalk.util.android.setDisplayedChild
import com.nmwilkinson.rooms.screen.roomslist.view.PhotoAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_active.*
import java.util.*


class ActiveActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ActiveContract.View {
    private val TAG: String = ActiveActivity::class.java.simpleName

    private val REQUEST_CODE_LOCATION_PERMISSION = 10

    private val KEY_ACTIVE_WALK = "walkActive"
    private val KEY_ACTIVE_IMAGES = "walkImages"

    private val fastestInterval = if (BuildConfig.DEBUG) 500L else 5000L
    private val interval = if (BuildConfig.DEBUG) 1000L else 60000L

    private var googleClient: GoogleApiClient? = null

    private var presenter: ActivePresenter? = null

    private var downloadStateReceiver: DownloadStateReceiver? = null

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var imageDownloader: FlickrSearchService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active)

        googleClient = GoogleApiClient.Builder(this, this, this)
                .addApi(LocationServices.API)
                .build()

        startButton.clicks().subscribe { presenter?.start() }
        stopButton.clicks().subscribe { presenter?.stop() }

        photoList.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        photoAdapter = PhotoAdapter()
        photoList.adapter = photoAdapter
    }

    override fun onDestroy() {
        super.onDestroy()

        destroyImageBroadcastReceiver()
    }

    override fun onStart() {
        super.onStart()

        googleClient?.connect()
    }

    override fun onStop() {
        super.onStop()

        googleClient?.disconnect()
    }

    private fun startLocationServices() {
        imageDownloader = Flickr.createImageDownloader()
        downloadStateReceiver = DownloadStateReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(downloadStateReceiver, IntentFilter(WalkingLocationHandler.Consts.BROADCAST_ACTION))

        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setFastestInterval(fastestInterval)
                .setInterval(interval)
                .setSmallestDisplacement(100.0f)
        val pendingIntent = createPendingIntent()
        LocationServices.FusedLocationApi.requestLocationUpdates(googleClient, locationRequest, pendingIntent)
    }

    override fun updateUI(state: ActiveContract.UIState) {
        when (state.active) {
            true -> {
                flipper.setDisplayedChild(photoListContainer)
                photoAdapter.setPhotos(state.imageUrls)
            }
            else -> {
                photoAdapter.clear()
                flipper.setDisplayedChild(startButton)
            }
        }

        saveState(state)
    }

    override fun startLocationTracking() {
        startLocationServices()
    }

    override fun stopLocationTracking() {
        destroyImageBroadcastReceiver()
        LocationServices.FusedLocationApi.removeLocationUpdates(googleClient, createPendingIntent())
    }

    private fun quitAppNoLocation() {
        errorDialog(this, getString(R.string.error_requires_permisson))
        finish()
    }

    override fun onConnected(connection: Bundle?) {
        presenter = ActivePresenter(loadStateAsModel())
        presenter?.setView(this)

        if (!isLocationGranted()) {
            requestLocationPermission(REQUEST_CODE_LOCATION_PERMISSION)
        }
    }

    override fun onConnectionSuspended(reason: Int) {
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, 0)
            } catch (e: IntentSender.SendIntentException) {
                errorDialog(this, e.localizedMessage)
            }
        } else {
            errorDialog(this, connectionResult.errorMessage ?: getString(R.string.error_unknown_playservices))
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            Log.i(TAG, "permission request result, granted $granted")
            if (granted) {
                startLocationServices()
            } else {
                quitAppNoLocation()
            }
        }
    }

    private fun destroyImageBroadcastReceiver() {
        downloadStateReceiver?.let { LocalBroadcastManager.getInstance(this).unregisterReceiver(it) }
    }

    private fun loadStateAsModel() = ActiveModel(SharedPreferencesHelper[this, KEY_ACTIVE_WALK, false], SharedPreferencesHelper[this, KEY_ACTIVE_IMAGES, listOf()])
    private fun saveState(state: ActiveContract.UIState) {
        SharedPreferencesHelper.save(this, KEY_ACTIVE_WALK, state.active)
        SharedPreferencesHelper.save(this, KEY_ACTIVE_IMAGES, state.imageUrls)
    }

    inner class DownloadStateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.getParcelableExtra<Location>(WalkingLocationHandler.Consts.BROADCAST_PARAM_LOCATION)?.let { location ->
                imageDownloader.imagesAtLocation(FlickrSearchService.Consts.methodSearch, Flickr.apiKey, location.latitude, location.longitude)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.i(TAG, "photo results $it")
                            it.photos.photoList.random()?.let { presenter?.newImage(it.formUrl()) }
                        }, {
                            errorDialog(this@ActiveActivity, "Failed to retrieve images")
                        })
            }
        }
    }
}

private fun List<FlickrPhoto>.random(): FlickrPhoto? {
    return if (isEmpty()) null else this[Random(SystemClock.uptimeMillis()).nextInt(size)]
}

