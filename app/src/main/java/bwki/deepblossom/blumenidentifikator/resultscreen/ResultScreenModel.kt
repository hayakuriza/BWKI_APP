//  TODO: Quantized Model zum Laufen bringen

package bwki.deepblossom.blumenidentifikator.resultscreen

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bwki.deepblossom.blumenidentifikator.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ResultScreenModel : ViewModel(),
    MainActivity.GlobalMethods, CoroutineScope {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    override val coroutineContext: CoroutineContext
        get() = viewModelJob + Dispatchers.Default

    private lateinit var imageClassification: ImageClassification
    //   var numResults = getPrefs().getString("numResults", "3")?.toInt()
    var numResults = 3
    // var confidenceThreshold = getPrefs().getString("confidenceThreshold", "$DEFAULT_CONFIDENCE_THRESHOLD")?.toFloat()
    var confidenceThreshold: Float = 0.1F
    val db = FirebaseFirestore.getInstance()

    var resultList = MutableLiveData<List<Result>>()

    var done = false


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

    }

    fun loadModule(labelLang: String) {
        imageClassification =
            ImageClassification.create(
                //TODO Quantized und Float unterscheiden (über Option)
                // TODO Füge Option für Ändern des Devices (falls unterstützt) und Anzahl der Threads hinzu
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
        val resultArray = mutableListOf<List<Result>>()
        for (i in 1..bundle.getInt("numberPictures")) {
            val results = async { imageClassification.classifyImage(bundle.getString("file$i")!!) }
            resultArray.add(results.await())
            Log.e("ResultScreenModel", "ResultArray: $resultArray")
        }

        val results = async { imageClassification.classifyImage(bundle.getString("file1")!!) }
        withContext(Dispatchers.Main) {
            Log.d("Results", results.await().toString())
            resultList.postValue(results.await())
        }
    }

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
