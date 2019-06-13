package de.bwki.blumenidentifikator


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import de.bwki.blumenidentifikator.databinding.FragmentResultBinding

/**
 * Hier werden die Ergebnisse des Netzes angezeigt.
 * Das Bild bekommt er vom MainScreenModel und wird hier verarbeitet.
 *
 */

class ResultFragment : Fragment(), MainActivity.GlobalMethods {

    lateinit var viewModel: ResultScreenModel
    private lateinit var classifier: ImageClassifier
    private var part: Int = 0

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

        binding.imageView.setImageBitmap(bitmapResized)
        viewModel.classify(bitmapResized)

        val imageViewList =
            arrayOf(binding.imageView2, binding.imageView3, binding.imageView4, binding.imageView5, binding.imageView6)

        // imageViewList[2].setImageResource(R.drawable.`0`)
        binding.resultScreenModel = viewModel
        binding.lifecycleOwner = this

/*        classifier = ImageClassifier(getAsset())
        classifier.recognizeImage(bitmapResized).subscribeBy(
            onSuccess = {
                binding.resultObject.text = it.toString()
            }
        )*/


//        val adapter = ResultScreenAdapter()
//        binding.resultList.adapter = adapter
//        viewModel.res
//       ultList.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                adapter.data = it
//            }
//        })

        // viewModel.resultText.observe(this, Observer { result ->
        //     binding.resultObject.text = result.toString()
        // })

        viewModel.part.observe(this, Observer {
            part = it
        })

        viewModel.visibil2.observe(this, Observer { visibil2 ->
            Log.d("ResultFragment", "visible changed")
            if (visibil2 == "VISIBLE") {
                binding.progressBar2.visibility = ProgressBar.VISIBLE
                binding.resultObject2.visibility = ProgressBar.VISIBLE
            } else {
                binding.progressBar2.visibility = ProgressBar.GONE
                binding.resultObject2.visibility = ProgressBar.GONE
            }
        })

        viewModel.visibil3.observe(this, Observer { visibil3 ->
            if (visibil3 == "VISIBLE") {
                binding.progressBar3.visibility = ProgressBar.VISIBLE
                binding.resultObject3.visibility = ProgressBar.VISIBLE
            } else {
                binding.progressBar3.visibility = ProgressBar.GONE
                binding.resultObject3.visibility = ProgressBar.GONE
            }
        })

        viewModel.visibil4.observe(this, Observer { visibil4 ->
            if (visibil4 == "VISIBLE") {
                binding.progressBar4.visibility = ProgressBar.VISIBLE
                binding.resultObject4.visibility = ProgressBar.VISIBLE
            } else {
                binding.progressBar4.visibility = ProgressBar.GONE
                binding.resultObject4.visibility = ProgressBar.GONE
            }
        })

        viewModel.visibil5.observe(this, Observer { visibil5 ->
            if (visibil5 == "VISIBLE") {
                binding.progressBar5.visibility = ProgressBar.VISIBLE
                binding.resultObject5.visibility = ProgressBar.VISIBLE
            } else {
                binding.progressBar5.visibility = ProgressBar.GONE
                binding.resultObject5.visibility = ProgressBar.GONE
            }
        })

        viewModel.imageView.observe(this, Observer {
            imageViewList[part].setImageResource(it)
        })



        return binding.root
    }

    //TODO Falls Internetverbindung weitere Informationen über Blume aus dem Internet holen
    //TODO? Image Analysis  nutzen um Motiv einer Blume zu erkennen und dann an Klassifikator schicken?
}
