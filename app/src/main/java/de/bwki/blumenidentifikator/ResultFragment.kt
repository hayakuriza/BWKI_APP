package de.bwki.blumenidentifikator


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import de.bwki.blumenidentifikator.databinding.FragmentResultBinding

/**
 * Hier werden die Ergebnisse des Netzes angezeigt.
 * Das Bild bekommt er vom MainScreenModel und wird hier verarbeitet.
 *
 */

class ResultFragment : Fragment(), MainActivity.GlobalMethods{

    lateinit var viewModel: ResultScreenModel
    private lateinit var classifier: ImageClassifier

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentResultBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false)
        val viewModel = ViewModelProviders.of(this).get(ResultScreenModel::class.java)
        viewModel.loadModule()
        viewModel.loadModule2()


        Log.d("ResultFragment", "OnCreateView called")
        setHasOptionsMenu(true)

        // Hole Bilddatei und konvertiere es in Bitmap, damit es angezeigt werden kann.
        // HINWEIS: ImageView ist um 90 Grad gedreht, um Bild gerade anzuzeigen.
        // HINWEIS: Bilder werden im Format YUV_422_888 gespeichert
        //val bitmap:Bitmap? = arguments!!.getParcelable("image")
        //binding.imageView.setImageBitmap(bitmap)

        //viewModel.classify(bitmap!!)
        val imageFile = arguments!!.getString("file")
        val bitmapResized = Bitmap.createScaledBitmap(
            BitmapFactory.decodeFile(imageFile),
            DEFAULT_INPUT_SIZE,
            DEFAULT_INPUT_SIZE,
            false
        )
        Log.e(
            "Image",
            "Height ${bitmapResized!!.height} Width ${bitmapResized.width} Color ${bitmapResized.colorSpace}"
        )

        binding.imageView.setImageBitmap(bitmapResized)
         viewModel.classify(bitmapResized)

        binding.resultScreenModel = viewModel
        binding.lifecycleOwner = this

/*        classifier = ImageClassifier(getAsset())
        classifier.recognizeImage(bitmapResized).subscribeBy(
            onSuccess = {
                binding.resultObject.text = it.toString()
            }
        )*/
        return binding.root
    }




    //TODO? Die Ergebnisse mithilfe eines RecyclerViews und Adapter anzeigen lassen?
    //TODO Falls Internetverbindung weitere Informationen über Blume aus dem Internet holen
    //TODO? Image Analysis  nutzen um Motiv einer Blume zu erkennen und dann an Klassifikator schicken?
}
