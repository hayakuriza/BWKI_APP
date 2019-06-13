package de.bwki.blumenidentifikator


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.crashlytics.android.Crashlytics


/**
 * A simple [Fragment] subclass.
 *
 */
class AboutFragment : Fragment(), MainActivity.GlobalMethods {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view!!.findViewById<Button>(R.id.button).setOnClickListener {
            Crashlytics.getInstance().crash()
        }
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    fun forceCrash(view: View) {
        Crashlytics.getInstance().crash()
    }

}
