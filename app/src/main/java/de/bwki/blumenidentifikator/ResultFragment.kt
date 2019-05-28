package de.bwki.blumenidentifikator


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import de.bwki.blumenidentifikator.databinding.FragmentResultBinding
import kotlinx.android.synthetic.main.fragment_result.*
import java.io.File

/**
 * Hier werden die Ergebnisse des Netzes angezeigt.
 * Das Bild bekommt er vom MainScreenModel und wird hier verarbeitet.
 *
 */

class ResultFragment : Fragment(), MainActivity.GlobalMethods {

    lateinit var viewModel: ResultScreenModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentResultBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false)
        val resultScreenModel = ViewModelProviders.of(this).get(ResultScreenModel::class.java)

        Log.d("ResultFragment", "OnCreateView called")

        // Hole Bilddatei und konvertiere es in Bitmap, damit es angezeigt werden kann.
        // HINWEIS: ImageView ist um 90 Grad gedreht, um Bild gerade anzuzeigen.
        val filePath = arguments!!.getString("file")
        val bitmap: Bitmap = BitmapFactory.decodeFile(filePath)
        binding.imageView.setImageBitmap(bitmap)

        binding.resultScreenModel = resultScreenModel
        binding.lifecycleOwner = this
        return binding.root
    }
}
