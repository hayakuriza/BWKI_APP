package bwki.deepblossom.blumenidentifikator.resultscreen

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bwki.deepblossom.blumenidentifikator.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Logik für ResultFragment
 */

class ResultScreenModel : ViewModel(),
    MainActivity.GlobalMethods, CoroutineScope {
    private var viewModelJob = Job()
    override val coroutineContext: CoroutineContext
        get() = viewModelJob + Dispatchers.Default

    private lateinit var imageClassification: ImageClassification
    private var numResults: Int = getPrefs().getString("numResults", "3")!!.toInt()
    private var confidenceThreshold: Float =
        getPrefs().getString("confidenceThreshold", "$DEFAULT_CONFIDENCE_THRESHOLD")!!.toFloat()

    var resultList = MutableLiveData<List<Result>>()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun loadModule(labelLang: String) {
        imageClassification =
            ImageClassification.create(
                //TODO Quantized und Float unterscheiden (über Option) ! Quantized funktioniert nicht, vermutlich Bug (TFLite ist im Alpha-Status)
                //TODO Füge Option für Ändern des Devices (falls unterstützt) und Anzahl der Threads hinzu
                classifierModel = ClassifierModel.FLOAT,
                assetManager = getAsset(),
                //   modelPath = getPrefs().getString("modelFile", "model.tflite")!!,
                modelPath = MODEL_FILE_PATH,
                labelPath = labelLang,
                confidenceThreshold = confidenceThreshold,
                numberOfResults = numResults,
                device = Device.CPU
            )
    }

    // TODO: mehrere Bilder klassifizieren und Durchschnitt berechnen
    fun classify(bundle: Bundle) = launch {
        //        val resultArray = mutableListOf<List<Result>>()
//        for (i in 1..bundle.getInt("numberPictures")) {
//            val results = async { imageClassification.classifyImage(bundle.getString("file$i")!!) }
//            resultArray.add(results.await())
//            Log.e("ResultScreenModel", "ResultArray: $resultArray")
//        }
        val results = async { imageClassification.classifyImage(bundle.getString("file1")!!) }
        withContext(Dispatchers.Main) {
            Log.d("Results", results.await().toString())
            resultList.postValue(results.await())
        }
    }


    // TODO: Parse Ergebnisse
//    fun updateTrueDatabase(string: String) {
//        val query = ParseQuery.getQuery<ParseObject>("Blumen")
//        query.whereEqualTo("Blumenname", string)
//        query.findInBackground { list, e ->
//            if (e == null) {
//                list[0].increment("AnzahlRichtig")
//            } else {
//                Log.d("DB-Error", "Error: " + e.message)
//            }
//        }
//    }
//
//    fun updateFalseDatabase(string: String) {
//        val query = ParseQuery.getQuery<ParseObject>("Blumen")
//        query.whereEqualTo("Blumenname", string)
//        query.findInBackground { list, e ->
//            if (e == null) {
//                list[0].increment("AnzahlFalsch")
//            } else {
//                Log.d("DB-Error", "Error: " + e.message)
//            }
//        }
//    }

}
