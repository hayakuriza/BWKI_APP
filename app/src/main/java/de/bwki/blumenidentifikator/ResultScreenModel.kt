package de.bwki.blumenidentifikator

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ResultScreenModel: ViewModel(), MainActivity.GlobalMethods, CoroutineScope{
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    override val coroutineContext: CoroutineContext
        get() = viewModelJob + Dispatchers.Default

    private lateinit var imageClassification : ImageClassification
    private lateinit var classifier: ImageClassifier

    var resultText1 = MutableLiveData<String>()
    var progress1 = MutableLiveData<Int>()
    var resultText2 = MutableLiveData<String>()
    var progress2 = MutableLiveData<Int>()
    var visibil2 = MutableLiveData<String>()
    var resultText3 = MutableLiveData<String>()
    var progress3 = MutableLiveData<Int>()
    var visibil3 = MutableLiveData<String>()



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
    }

    fun loadModule(){
            imageClassification = ImageClassification.create(
                classifierModel = ClassifierModel.QUANTIZED,
                assetManager = getAsset(),
                modelPath = MODEL_FILE_PATH,
                labelPath = LABELS_FILE_PATH
            )
        }

    fun loadModule2(){
        classifier = ImageClassifier(getAsset())
    }


    fun classify(bitmap:Bitmap) = launch {
        val results = async { imageClassification.classifyImage(bitmap) }
        var done = false

        Log.d("Results", results.await().toString())
        withContext(Dispatchers.Main){
         //   resultText.value = results.await().toString()
         //   resultList.value = results.await()
            resultText1.value = results.await()[0].toString()
            progress1.value = (results.await()[0].confidence * 100).toInt()

            Log.d("Results", results.await().size.toString())
            if (results.await().size > 1){
                resultText2.value = results.await()[1].toString()
                progress2.value = (results.await()[1].confidence * 100).toInt()
                visibil2.value = "VISIBLE"
            }; if (results.await().size > 2) {
                resultText3.value = results.await()[2].toString()
                progress3.value = (results.await()[2].confidence * 100).toInt()
                visibil3.value = "VISIBLE"
             }
            done = true
        }
    }
}
