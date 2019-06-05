package de.bwki.blumenidentifikator

import android.Manifest
import android.app.Application
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.Surface
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
        val previewConfig = PreviewConfig.Builder().apply() {
            setTargetAspectRatio(Rational(1,1))
            setTargetResolution(Size(640,640))
        }.build()

        return Preview(previewConfig)
        }

    fun cameraStart2(): ImageCaptureConfig {
        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            setTargetAspectRatio(Rational(1,1))
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            setTargetRotation(Surface.ROTATION_0)
        }.build()
        return imageCaptureConfig
    }

    fun checkFirstStart(): Boolean{
        return (!getPrefs().getBoolean("firstStart", false))
    }
}
