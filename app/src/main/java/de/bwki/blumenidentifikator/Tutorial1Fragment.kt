package de.bwki.blumenidentifikator


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import de.bwki.blumenidentifikator.databinding.FragmentTutorial1Binding


/**
 * First Tutorial Screen
 *
 */
class Tutorial1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflate<FragmentTutorial1Binding>(inflater, R.layout.fragment_tutorial1, container, false)

        return binding.root
    }




}
