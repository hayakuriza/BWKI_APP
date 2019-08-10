package bwki.deepblossom.blumenidentifikator.tutorial


import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import bwki.deepblossom.blumenidentifikator.MainActivity
import bwki.deepblossom.blumenidentifikator.R
import bwki.deepblossom.blumenidentifikator.databinding.FragmentTutorial2Binding
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Tutorial 2
 *
 */
class Tutorial2Fragment : Fragment(),
    MainActivity.GlobalMethods {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTutorial2Binding = inflate(
            inflater,
            R.layout.fragment_tutorial2, container, false
        )
        lockDrawer()

        binding.buttonFinish.setOnClickListener { view: View ->
            //Hier werden alle Einstellungen auf ihre Standardwerte gesetzt,
            // da dieses Fragment nur ein einziges Mal aufgerufen wird
            getPrefs().edit().putBoolean("firstStart", true).apply()
            getPrefs().edit().putBoolean("useInternetInfos", true).apply()
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 10)
        }

        binding.buttonZurueck.setOnClickListener {
            NavigationUI.navigateUp(findNavController(), drawerLayout)
        }
        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        view!!.findNavController().navigate(R.id.action_tutorial2Fragment_to_mainScreenFragment)
    }
}
