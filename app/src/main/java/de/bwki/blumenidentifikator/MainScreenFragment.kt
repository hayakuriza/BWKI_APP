package de.bwki.blumenidentifikator

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import com.google.android.material.snackbar.Snackbar
import de.bwki.blumenidentifikator.databinding.FragmentMainScreenBinding
import java.io.File

/**
 * Main Screen
 * Hier wird das Bild geschossen.
 *
 */

class MainScreenFragment : Fragment(), MainActivity.GlobalMethods {

    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    private val REQUEST_CODE = 10
    private lateinit var viewFinder: TextureView
    lateinit var viewModel: MainScreenModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil.inflate<FragmentMainScreenBinding>(
                inflater,
                R.layout.fragment_main_screen,
                container,
                false
            )
        val application = requireNotNull(this.activity).application
        val viewModelFactory = MainScreenModelFactory(application)
        setHasOptionsMenu(true) //Overflow Menu


        // ViewModel
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainScreenModel::class.java)
        binding.mainScreenModel = viewModel
        binding.lifecycleOwner = this

        if (viewModel.checkFirstStart()) {
            findNavController().navigate(R.id.action_mainScreenFragment_to_tutorial1Fragment)
        }

        // Kamera
        viewFinder = binding.viewFinder

        val preview = viewModel.startCamera()
        preview.setOnPreviewOutputUpdateListener {
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)
            viewFinder.surfaceTexture = it.surfaceTexture
        }

        binding.focusButton.setOnClickListener {
            // TODO: understand Dimensions of the sensor coordinate frame and focus on center
            // TODO Advanced: focus on area tapped by user
            preview.focus(Rect(0, 0, 0, 0), Rect(0, 0, 0, 0))
            Log.e("focus_button", "focus!")
        }

        // Schieße Bild und schicke es an ResultFragment
        // TODO:?? Bild so abspeichern, dass es um 90 Grad gedreht ist?
        // TODO: Möglichkeit, mehrere Bilder zu machen
        val imageCapture = ImageCapture(viewModel.cameraStart2())
        binding.imageButton.setOnClickListener {
            val file = File(context?.filesDir, "image.jpg")
            imageCapture.takePicture(file,
                object : ImageCapture.OnImageSavedListener {
                    override fun onError(
                        error: ImageCapture.UseCaseError,
                        message: String, exc: Throwable?
                    ) {
                        val msg = "Photo capture failed: $message"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        Log.e("CameraXApp", msg)
                        exc?.printStackTrace()
                    }

                    override fun onImageSaved(file: File) {
                        val msg = "Photo capture succeeded: ${file.absolutePath}"
                        Log.d("CameraXApp", msg)
                        var bundle = bundleOf("file" to file.absolutePath)
                        findNavController().navigate(R.id.action_mainScreenFragment_to_resultFragment, bundle)
                    }
                })
        }

        /* binding.imageButton.setOnClickListener {
            imageCapture.takePicture(object : ImageCapture.OnImageCapturedListener() {
                override fun onError(
                    error: ImageCapture.UseCaseError,
                    message: String, exc: Throwable?
                ) {
                    val msg = "Photo capture failed: $message"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    Log.e("CameraXApp", msg)
                    exc?.printStackTrace()
                }

                override fun onCaptureSuccess(image: ImageProxy?, rotationDegrees: Int) {
                    Log.d("CameraX", "Success")
                    val buffer: ByteBuffer = image!!.image!!.planes[0].buffer
                    val bytes = ByteArray(buffer.remaining())
                    buffer.get(bytes)
                    val bundle = Bundle()
                    bundle.putParcelable("image", BitmapFactory.decodeByteArray(bytes,0,bytes.size))
                   // val format = image.format
                   // Log.e("CameraX", "Format: $format")
                    findNavController().navigate(R.id.action_mainScreenFragment_to_resultFragment, bundle)
                }
            })
        } */

        CameraX.bindToLifecycle(this, preview, imageCapture)

        if (allPermissionsGranted()) {
            viewFinder.post { viewModel.startCamera() }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        CameraX.unbindAll()
    }

    // Für Overflow Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return onNavDestinationSelected(item, view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this.context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.permissiob_not_granted), Snackbar.LENGTH_LONG
                ).show()
                return false
            }
        }
        return true
    }
}
