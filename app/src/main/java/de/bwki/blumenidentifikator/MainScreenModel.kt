package de.bwki.blumenidentifikator

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel

// Hier steht die ganze Logik

class MainScreenModel: ViewModel(), MainActivity.GlobalMethods {

    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    private val REQUEST_CODE = 10

    init {
        Log.i("MainScreenModel", "Model created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("MainScreenModel", "Model destroyed")
    }

    // Für Kamera
    fun startCamera() {

    }

    fun updateTransform() {

    }

    fun allPermissionsGranted(context: Context): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun checkFirstStart(): Boolean{
        return (!getPrefs().getBoolean("firstStart", false))
    }
}
