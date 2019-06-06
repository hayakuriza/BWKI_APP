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

    private var resultList = MutableLiveData<Array<String>>()
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
       // resultText = results.toString()
        Log.d("Results", results.await().toString())
        withContext(Dispatchers.Main){
           var restul = results.await().toString()
        }
    }

}
