//  TODO: Quantized Model zum Laufen bringen

package de.bwki.blumenidentifikator

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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
    var confidenceThreshold = getPrefs().getString("confidenceThreshold", "$DEFAULT_CONFIDENCE_THRESHOLD")?.toFloat()
    val db = FirebaseFirestore.getInstance()

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
    var part = MutableLiveData<Int>()
    val imageView = MutableLiveData<Int>()
    val imagePreview = mapOf<Int, Int>(
        0 to R.drawable.alpineseaholly,
        1 to R.drawable.anthurium,
        2 to R.drawable.artichoke,
        3 to R.drawable.azalea,
        4 to R.drawable.ballmoss, // funktioniert nicht! keine richtige klassifizierung!
        5 to R.drawable.balloonflower,
        6 to R.drawable.barbertondaisie, // nur die gelbe funktioniert! Barberton Daisy ist eine Art!
        7 to R.drawable.beardediris, // unzuverlässig
        8 to R.drawable.beebalm,
        9 to R.drawable.birdofparadise,
        10 to R.drawable.bishopofllandaff,
        11 to R.drawable.blackeyedsusan,
        12 to R.drawable.blackberrylily,
        13 to R.drawable.blanketflower,
        14 to R.drawable.bolerodeepblue,
        15 to R.drawable.bougainviella,
        16 to R.drawable.bromelia,
        17 to R.drawable.buttercup,
        18 to R.drawable.californianpoppy,
        19 to R.drawable.camellia, // etwas ungenau
        // 20 to canna lily // funktioniert nicht
        21 to R.drawable.canterburybells,
        // 22 to capeflower // finde ich nicht
        23 to R.drawable.carnation,
        24 to R.drawable.cautleyaspicata, // keine richtige klassifizierung
        25 to R.drawable.clematis, // funktioniert nur mit der lilalen
        26 to R.drawable.coltsfoot,
        27 to R.drawable.columbine, // etwas unzuverlässig
        28 to R.drawable.commondandelion,
        29 to R.drawable.cornpoppy,
        30 to R.drawable.cyclamen, // unzuverlässig
        31 to R.drawable.daffodil,
        32 to R.drawable.desertrose,
        33 to R.drawable.englishmarigold, // funktioniert nur mit der orangenen gut
        34 to R.drawable.firelily, // unzuverlässig
        35 to R.drawable.foxglove, // etwas unzuverlässig
        36 to R.drawable.frangipani,
        37 to R.drawable.fritillary,
        38 to R.drawable.gardenphlox,
        39 to R.drawable.gaura,
        40 to R.drawable.gazania,
        41 to R.drawable.geranium, // unzuverlässig
        42 to R.drawable.giantwhitearumlily,
        // 43 to globe flower find ich nicht
        44 to R.drawable.globethistle,
        45 to R.drawable.grapehyacinth,
        46 to R.drawable.greatmasterwort,
        47 to R.drawable.hardleavedpocketorchid, // etwas unzuverlässig
        48 to R.drawable.hibiscus,
        49 to R.drawable.hippeastrum, // nur die pinke wird erkannt. es gibt verschiedene hippeastrum
        50 to R.drawable.japaneseanemone, // nur die pinke funktioniert
        51 to R.drawable.kingprotea,
        52 to R.drawable.lentenrose, // unzuverlässig
        53 to R.drawable.lotus, // etwas unzuverlässig
        54 to R.drawable.lotusinthemist,
        55 to R.drawable.magnolia, // funktioniert nicht
        56 to R.drawable.mallow, // funktioniert nicht
        57 to R.drawable.marigold, // nur gelbe funktionieren
        58 to R.drawable.mexican_aster,
        59 to R.drawable.mexican_petunia,
        60 to R.drawable.monkshood, // etwas unzuverlässig
        61 to R.drawable.moon_orchid,
        62 to R.drawable.morning_glory,
        // 63 to orange dahlia find ich net
        64 to R.drawable.osteospermum, // es erkennt nur daisybush,eine Art von osteospermum
        65 to R.drawable.oxeye_daisy,
        66 to R.drawable.passionflower,

        97 to R.drawable.waterlily //Probleme mit klassifizieren
    )

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
ü    }

    fun loadModule() {
        imageClassification = ImageClassification.create(
            classifierModel = ClassifierModel.QUANTIZED,
            assetManager = getAsset(),
            modelPath = getPrefs().getString("modelFile", "model.tflite")!!,
            labelPath = LABELS_FILE_PATH,
            confidenceThreshold = confidenceThreshold!!,
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
            for (i in 0 until results.await().size) {
                Log.d("ResultNum", i.toString())
                Log.d("ResultText", resultTextList[i].toString())
                resultTextList[i].postValue(results.await()[i].name + " " + (results.await()[i].confidence * 100).toInt())
                progressList[i].postValue((results.await()[i].confidence * 100).toInt())
                //   imageView.value = imagePreview[(results.await()[i].id).toInt()]

                if (i > 0) {
                    visibilList[i]!!.postValue("VISIBLE")
                }
            }
            done = true
        }
    }

    fun updateTrueDatabase(string: String) {
        db.collection("blumen").document(string)
            .update("true", FieldValue.increment(1))
            .addOnSuccessListener {
                Log.d("DB-Update", "Successull update$string")
            }
            .addOnFailureListener {
                Log.e("DB-Update", "Error updating $string")
            }
    }

    fun updateFalseDatabase(string: String) {
        db.collection("blumen").document(string)
            .update("false", FieldValue.increment(1))
            .addOnSuccessListener {
                Log.d("DB-Update", "Successull update$string")
            }
            .addOnFailureListener {
                Log.e("DB-Update", "Error updating $string")
            }
    }
}
