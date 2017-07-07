package com.nmwilkinson.photowalk

import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.android.gms.location.LocationResult

class WalkingLocationHandler : IntentService("com.nmwilkinson.photowalk.location") {
    private val TAG: String = WalkingLocationHandler::class.java.simpleName

    object Consts {
        val BROADCAST_ACTION = "com.nmwilkinson.photowalk.BROADCAST"
        val BROADCAST_PARAM_LOCATION = "location"
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.i(TAG, "onHandleIntent()")
        if (LocationResult.hasResult(intent)) {
            val location = LocationResult.extractResult(intent).lastLocation
            Log.i(TAG, "- $location")

            val localIntent = Intent(Consts.BROADCAST_ACTION).putExtra(Consts.BROADCAST_PARAM_LOCATION, location)
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent)
        }
    }
}

fun Context.createPendingIntent() = PendingIntent.getService(this, 0, Intent(this, WalkingLocationHandler::class.java), PendingIntent.FLAG_UPDATE_CURRENT)