package com.nmwilkinson.photowalk.location

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

/**
 * Location Permissions utilities.
 */
object LocationPermissions {
    val LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION
}

fun Context.isLocationGranted(): Boolean {
    return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, LocationPermissions.LOCATION_PERMISSION)
}

fun AppCompatActivity.shouldShowLocationRequestPermissionRationale(): Boolean {
    return shouldShowRequestPermissionRationale(LocationPermissions.LOCATION_PERMISSION)
}

fun Activity.requestLocationPermission(activityRequestCode: Int) {
    ActivityCompat.requestPermissions(this, arrayOf(LocationPermissions.LOCATION_PERMISSION), activityRequestCode)
}
