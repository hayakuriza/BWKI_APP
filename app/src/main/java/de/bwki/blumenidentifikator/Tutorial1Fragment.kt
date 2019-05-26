package de.bwki.blumenidentifikator


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.inflate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import de.bwki.blumenidentifikator.databinding.FragmentTutorial1Binding
import kotlinx.android.synthetic.main.activity_main.*


/**
 * First Tutorial Screen
 *
 * Wird angezeigt, wenn die App zum ersten Mal gestartet wird. Am Ende des Tutorials soll die Permission für die Kamera
 * abgefragt werden.
 */
class Tutorial1Fragment : Fragment(), MainActivity.GlobalMethods{

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTutorial1Binding= inflate(inflater, R.layout.fragment_tutorial1, container, false)
        lockDrawer()

        binding.buttonNext.setOnClickListener {view: View ->
            view.findNavController().navigate(R.id.action_tutorial1Fragment_to_tutorial2Fragment)
        }

        return binding.root
    }
}
