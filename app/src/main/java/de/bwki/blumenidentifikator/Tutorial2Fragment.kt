package de.bwki.blumenidentifikator


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil.inflate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import de.bwki.blumenidentifikator.databinding.FragmentTutorial2Binding
import android.Manifest
import android.content.SharedPreferences
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Tutorial 2
 *
 */
class Tutorial2Fragment : Fragment(), MainActivity.GlobalMethods {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTutorial2Binding = inflate(inflater, R.layout.fragment_tutorial2, container, false)
        lockDrawer()

        binding.buttonFinish.setOnClickListener{view: View ->
            getPrefs().edit().putBoolean("firstStart", true).apply()
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 10)
        }

        binding.buttonZurueck.setOnClickListener{
            NavigationUI.navigateUp(findNavController(), drawerLayout)
        }
        return binding.root
        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        view!!.findNavController().navigate(R.id.action_tutorial2Fragment_to_mainScreenFragment)
    }
}
