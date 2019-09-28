package bwki.deepblossom.blumenidentifikator.resultscreen


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import bwki.deepblossom.blumenidentifikator.*
import bwki.deepblossom.blumenidentifikator.databinding.FragmentResultBinding

/**
 * Hier werden die Ergebnisse des Netzes angezeigt.
 * Das Bild bekommt er vom MainScreenModel und wird dann bei ImageClassification verarbeitet.
 */

class ResultFragment : Fragment(), MainActivity.GlobalMethods {

    private lateinit var labelLang: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentResultBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_result, container, false
            )
        val viewModel = ViewModelProviders.of(this).get(ResultScreenModel::class.java)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        val use_german: Boolean = sharedPreferences.getBoolean("use_german", false)
        labelLang = if (use_german) {
            LABELS_FILE_PATH_DE
        } else {
            LABELS_FILE_PATH
        }
        Log.i("ResultFragment", "Label: $labelLang")
        viewModel.loadModule(labelLang)

        Log.d("ResultFragment", "OnCreateView called")
        setHasOptionsMenu(true)

        // Hole Bilddatei und konvertiere es in Bitmap, damit es angezeigt werden kann.
        // HINWEIS: ImageView ist um 90 Grad gedreht, um Bild gerade anzuzeigen.
        // HINWEIS: Bilder werden im Format YUV_422_888 gespeichert

        classify(arguments!!, viewModel, binding)

        binding.resultScreenModel = viewModel
        binding.lifecycleOwner = this

        val adapter = ResultScreenAdapter()
        binding.resultListHolder.adapter = adapter



        viewModel.resultList.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("ResultFragment", "Result Observer called: $it")
                adapter.data = it
            }
        })

        return binding.root
    }

    private fun classify(
        bundle: Bundle,
        viewModel: ResultScreenModel,
        binding: FragmentResultBinding
    ) {
        val bitmapResized = Bitmap.createScaledBitmap(
            BitmapFactory.decodeFile(bundle.getString("file1")),
            DEFAULT_INPUT_SIZE,
            DEFAULT_INPUT_SIZE,
            false
        )
        binding.imageView.setImageBitmap(bitmapResized)
        viewModel.classify(bundle)

    }

    //TODO Falls Internetverbindung weitere Informationen über Blume aus dem Internet holen
    //TODO? Image Analysis  nutzen um Motiv einer Blume zu erkennen und dann an Klassifikator schicken?
}
