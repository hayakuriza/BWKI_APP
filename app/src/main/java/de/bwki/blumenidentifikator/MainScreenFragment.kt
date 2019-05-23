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
import android.util.Size
import android.graphics.Matrix
import java.util.concurrent.TimeUnit

/**
 * The Main Screen
 *
 */

class MainScreenFragment : Fragment() {

    private lateinit var viewFinder: TextureView

    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    private val REQUEST_CODE = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil.inflate<FragmentMainScreenBinding>(inflater, R.layout.fragment_main_screen, container, false)
        setHasOptionsMenu(true) //Overflow Menu

        // Kamera
        viewFinder = binding.viewFinder

        // Kamera Permission
        if (allPermissionsGranted()) {
            viewFinder.post { startCamera()}
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE)
        }
        return binding.root
    }

    // Für Overflow Menu
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return onNavDestinationSelected(item!!, view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    // Für Kamera
    private fun startCamera() {

    }

    private fun updateTransform(){

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (allPermissionsGranted()) {
            viewFinder.post { startCamera()}
        } else {
            Toast.makeText(context!!, "Permissions not granted", Toast.LENGTH_SHORT).show()
           // finish()
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}
