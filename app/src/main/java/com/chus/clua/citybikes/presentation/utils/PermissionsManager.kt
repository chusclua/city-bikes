package com.chus.clua.citybikes.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

fun doRequestPermissions(
    permission: String,
    requestCode: Int,
    owner: LifecycleOwner
) {
    when (owner) {
        is Activity -> ActivityCompat.requestPermissions(owner, arrayOf(permission), requestCode)
        is Fragment -> owner.requestPermissions(arrayOf(permission), requestCode)
        else -> throw IllegalArgumentException("Provided context must be either an Activity or a Fragment")
    }
}

fun hasPermissions(permission: String, context: Context) =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

fun shouldShowRationale(permission: String, activity: Activity) =
    ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)