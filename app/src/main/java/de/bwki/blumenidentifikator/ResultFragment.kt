package de.bwki.blumenidentifikator


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import de.bwki.blumenidentifikator.databinding.FragmentResultBinding

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
        setHasOptionsMenu(true)

        // Hole Bilddatei und konvertiere es in Bitmap, damit es angezeigt werden kann.
        // HINWEIS: ImageView ist um 90 Grad gedreht, um Bild gerade anzuzeigen.
        // HINWEIS: Bilder werden im Format YUV_422_888 gespeichert
        val filePath = arguments!!.getString("file")
        val bitmap: Bitmap = BitmapFactory.decodeFile(filePath)
        binding.imageView.setImageBitmap(bitmap)

        binding.resultScreenModel = resultScreenModel
        binding.lifecycleOwner = this
        return binding.root
    }

    //TODO? Die Ergebnisse mithilfe eines RecyclerViews und Adapter anzeigen lassen?
    //TODO Falls Internetverbindung weitere Informationen über Blume aus dem Internet holen
    //TODO? Image Analysis  nutzen um Motiv einer Blume zu erkennen und dann an Klassifikator schicken?
}
