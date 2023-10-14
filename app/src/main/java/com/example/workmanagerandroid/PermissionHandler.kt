package com.example.workmanagerandroid

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun hasPermission(context: Context, permissionName: String): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        permissionName
    ) == PackageManager.PERMISSION_GRANTED
}
fun requestManyPermission(activity: Activity,arrayOfPermissions: Array<String>, requestCode: Int) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOfPermissions,
        requestCode
    )
}