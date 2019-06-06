package de.bwki.blumenidentifikator

import android.Manifest
import android.app.Application
import android.util.Log
import android.util.Rational
import android.util.Size
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.lifecycle.AndroidViewModel
import de.bwki.blumenidentifikator.MainActivity.GlobalMethods

// Hier steht die ganze Logik

class MainScreenModel(application: Application): AndroidViewModel(application), GlobalMethods{

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
    fun startCamera(): Preview {
        // Config Object
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(Rational(1,1))
            setTargetResolution(Size(224,224))
        }.build()

        return Preview(previewConfig)
        }

    fun cameraStart2(): ImageCaptureConfig {
        return ImageCaptureConfig.Builder().apply {
            setTargetAspectRatio(Rational(1,1))
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            setTargetResolution(Size(224, 224))
        }.build()
    }

    fun checkFirstStart(): Boolean{
        return (!getPrefs().getBoolean("firstStart", false))
    }
}
