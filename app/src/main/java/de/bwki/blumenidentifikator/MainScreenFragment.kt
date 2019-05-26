package de.bwki.blumenidentifikator


import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import de.bwki.blumenidentifikator.databinding.FragmentMainScreenBinding
import org.w3c.dom.Text
import android.Manifest
import android.app.Activity
import android.content.SharedPreferences
import android.util.Size
import android.graphics.Matrix
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import java.util.concurrent.TimeUnit

/**
 * Main Screen
 *
 */

class MainScreenFragment : Fragment(){

    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    private val REQUEST_CODE = 10

    private lateinit var viewFinder: TextureView
    lateinit var viewModel: MainScreenModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil.inflate<FragmentMainScreenBinding>(inflater, R.layout.fragment_main_screen, container, false)
        setHasOptionsMenu(true) //Overflow Menu

        // ViewModel
        viewModel = ViewModelProviders.of(this).get(MainScreenModel::class.java)
        binding.mainScreenModel = viewModel
        binding.lifecycleOwner = this

        // Kamera
        viewFinder = binding.viewFinder

        cameraPermissionGet(REQUEST_CODE, REQUIRED_PERMISSIONS)

        if (viewModel.checkFirstStart()) {
            findNavController().navigate(R.id.action_mainScreenFragment_to_tutorial1Fragment)
        }

        return binding.root
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (viewModel.allPermissionsGranted(context!!)) {
            viewFinder.post { viewModel.startCamera()}
        } else {
            Toast.makeText(context!!, "Permissions not granted", Toast.LENGTH_SHORT).show()
        }
    }


    // Ich bekomme es nicht hin, die Methode im Model auszuführen, da ich nicht die Activity bekomme (im Fragment wird sie nicht benötigt)
    fun cameraPermissionGet(requestCode: Int, permissions:Array<String>) {
        requestPermissions(permissions, requestCode)
    }
}
