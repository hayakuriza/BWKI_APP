//  TODO: Quantized Model zum Laufen bringen

package de.bwki.blumenidentifikator

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ResultScreenModel : ViewModel(), MainActivity.GlobalMethods, CoroutineScope {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    override val coroutineContext: CoroutineContext
        get() = viewModelJob + Dispatchers.Default

    private lateinit var imageClassification: ImageClassification
    private lateinit var classifier: ImageClassifier
    var numResults = getPrefs().getString("numResults", "3")?.toInt()
    var confidenceThreshold = getPrefs().getFloat("confidenceThreshold", DEFAULT_CONFIDENCE_THRESHOLD)

    var resultText1 = MutableLiveData<String>()
    var progress1 = MutableLiveData<Int>()
    var resultText2 = MutableLiveData<String>()
    var progress2 = MutableLiveData<Int>()
    var visibil2 = MutableLiveData<String>()
    var resultText3 = MutableLiveData<String>()
    var progress3 = MutableLiveData<Int>()
    var visibil3 = MutableLiveData<String>()
    var resultText4 = MutableLiveData<String>()
    var progress4 = MutableLiveData<Int>()
    var visibil4 = MutableLiveData<String>()
    var resultText5 = MutableLiveData<String>()
    var progress5 = MutableLiveData<Int>()
    var visibil5 = MutableLiveData<String>()

    val visibilList = arrayOf(null, visibil2, visibil3, visibil4, visibil5)
    val resultTextList = arrayOf(resultText1, resultText2, resultText3, resultText4, resultText5)
    val progressList = arrayOf(progress1, progress2, progress3, progress4, progress5)


    val resultList = MutableLiveData<List<Recognizable>>()

    var done = false
    val resultText = MutableLiveData<String>()


    // Beispiel für Coroutines
    // TODO: Hier soll mit Coroutine das Bild ausgewertet werden. Zurückkommen soll eine List/Array
    private fun startSomething() {
        uiScope.launch {
            // Do something
            doSomethinginCoroutine()


            done = true
        }
    }

    private suspend fun doSomethinginCoroutine(): Array<String>? {
        return withContext(Dispatchers.IO) {
            //do Something und gib Array/list zurück
            null
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        visibil2.value = "GONE"
        visibil3.value = "GONE"
        visibil4.value = "GONE"
        visibil5.value = "GONE"
    }

    fun loadModule() {
        numResults = getPrefs().getString("numResults", "3")!!.toInt()
        confidenceThreshold =
            getPrefs().getString("confidenceThreshold", DEFAULT_CONFIDENCE_THRESHOLD.toString())!!.toFloat()
        imageClassification = ImageClassification.create(
            classifierModel = ClassifierModel.QUANTIZED,
            assetManager = getAsset(),
            modelPath = MODEL_FILE_PATH,
            labelPath = LABELS_FILE_PATH,
            confidenceThreshold = confidenceThreshold,
            numberOfResults = numResults!!
        )
    }

    fun loadModule2() {
        classifier = ImageClassifier(getAsset())
    }


    fun classify(bitmap: Bitmap) = launch {
        val results = async { imageClassification.classifyImage(bitmap) }
        var done = false

        Log.d("Results", results.await().toString())
        withContext(Dispatchers.Main) {
            //   resultText.value = results.await().toString()
            //   resultList.value = results.await()
            for (i in 0 until results.await().size) {
                Log.d("ResultNum", i.toString())
                Log.d("ResultText", resultTextList[i].toString())
                resultTextList[i].postValue(results.await()[i].name + " " + (results.await()[i].confidence * 100).toInt())
                progressList[i].postValue((results.await()[i].confidence * 100).toInt())
                if (i > 0) {
                    visibilList[i]!!.postValue("VISIBLE")
                }
            }
            done = true
        }
    }
}
