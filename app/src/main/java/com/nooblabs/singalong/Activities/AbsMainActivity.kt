package com.nooblabs.singalong.Activities

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log

abstract class AbsMainActivity : AppCompatActivity() {

    companion object {
        val STORAGE_READ_PERMISSION = 123
        val TAG = javaClass.simpleName
    }


    fun requestStoragePermission(): Boolean {
        Log.d(TAG, "Checking for Read Storage Permission")
        if (ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Requesting for Read Storage Permission")
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                TODO("Show why you are requesting permission. Dont block UI")
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        STORAGE_READ_PERMISSION)
            }
            return false
        } else {
            Log.d(TAG, "App has storage read permission.")
            return true
        }
    }

}
