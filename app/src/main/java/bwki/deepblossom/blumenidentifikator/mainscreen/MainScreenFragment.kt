package bwki.deepblossom.blumenidentifikator.mainscreen

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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import bwki.deepblossom.blumenidentifikator.MainActivity
import bwki.deepblossom.blumenidentifikator.R
import bwki.deepblossom.blumenidentifikator.databinding.FragmentMainScreenBinding
import bwki.deepblossom.blumenidentifikator.res
import com.google.android.material.snackbar.Snackbar
import java.io.File

/**
 * Main Screen
 * Hier wird das Bild geschossen.
 *
 */

class MainScreenFragment : Fragment(),
    MainActivity.GlobalMethods {

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
        val viewModelFactory =
            MainScreenModelFactory(application)
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
        val bundle = bundleOf("numberPictures" to 0)
        val numberPictures = MutableLiveData<Int>()
        numberPictures.postValue(0)

        binding.imageButton.setOnClickListener {
            val file = File(context?.filesDir, "image$numberPictures.jpg")
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
                        numberPictures.value!!.plus(1)
                        bundle.putString("file$numberPictures", file.absolutePath)
                        val current = numberPictures.value
                        bundle.putInt("numberPicture", current!!)
                    }
                })
        }

        binding.button2.setOnClickListener {
            if (numberPictures.value != 0) {
                findNavController().navigate(
                    R.id.action_mainScreenFragment_to_resultFragment,
                    bundle
                )
            } else {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    "Bitte mache mindestens ein Bild",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        CameraX.bindToLifecycle(this, preview, imageCapture)

        numberPictures.observe(viewLifecycleOwner, Observer {
            binding.numberPicturesText.text =
                String.format(res.getString(R.string.numberPictures), numberPictures.value)
        })

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


    private fun allPermissionsGranted(): Boolean {
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
