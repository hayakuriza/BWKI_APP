//  TODO: Quantized Model zum Laufen bringen

package de.bwki.blumenidentifikator

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ResultScreenModel : ViewModel(), MainActivity.GlobalMethods, CoroutineScope {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    override val coroutineContext: CoroutineContext
        get() = viewModelJob + Dispatchers.Default

    private lateinit var imageClassification: ImageClassification
    // var numResults = getPrefs().getString("numResults", 3)?.toInt()
    var numResults = 3
    // var confidenceThreshold = getPrefs().getString("confidenceThreshold", "$DEFAULT_CONFIDENCE_THRESHOLD")?.toFloat()
    var confidenceThreshold: Float = 0.1F
    val db = FirebaseFirestore.getInstance()

    var resultText1 = MutableLiveData<String>()
    var progress1 = MutableLiveData<Int>()
    var imageView2 = MutableLiveData<Int>()
    var resultText2 = MutableLiveData<String>()
    var progress2 = MutableLiveData<Int>()
    var visibil2 = MutableLiveData<String>()
    var imageView3 = MutableLiveData<Int>()
    var resultText3 = MutableLiveData<String>()
    var progress3 = MutableLiveData<Int>()
    var visibil3 = MutableLiveData<String>()
    var imageView4 = MutableLiveData<Int>()
    var resultText4 = MutableLiveData<String>()
    var progress4 = MutableLiveData<Int>()
    var visibil4 = MutableLiveData<String>()
    var imageView5 = MutableLiveData<Int>()
    var resultText5 = MutableLiveData<String>()
    var progress5 = MutableLiveData<Int>()
    var visibil5 = MutableLiveData<String>()
    var imageView6 = MutableLiveData<Int>()

    val visibilList = arrayOf(null, visibil2, visibil3, visibil4, visibil5)
    val resultTextList = arrayOf(resultText1, resultText2, resultText3, resultText4, resultText5)
    val progressList = arrayOf(progress1, progress2, progress3, progress4, progress5)
    val imageViewList = arrayOf(imageView2, imageView3, imageView4, imageView5, imageView6)
    val resultList = MutableLiveData<List<Recognizable>>()
    var part = MutableLiveData<Int>()
    var imageView = MutableLiveData<Int>()
    val imagePreview = mapOf<Int, Int>(
        0 to R.drawable.alpineseaholly,
        1 to R.drawable.anthurium,
        2 to R.drawable.artichoke,
        3 to R.drawable.azalea, // sind verschiedene Srten und Sorten der Gattung Rhododendron
        4 to R.drawable.ballmoss, // funktioniert nicht! keine richtige klassifizierung! Mooskugel Algenart
        5 to R.drawable.balloonflower, //anscheinend mehrere Farben
        6 to R.drawable.barbertondaisie, // nur die gelbe funktioniert!
        7 to R.drawable.beardediris, // Pflanzengattung
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
        23 to R.drawable.carnation, // Gattung
        24 to R.drawable.cautleyaspicata, // keine richtige klassifizierung
        25 to R.drawable.clematis, // funktioniert nur mit der lilalen // Gattung
        26 to R.drawable.coltsfoot,
        27 to R.drawable.columbine, // etwas unzuverlässig // Gattung
        28 to R.drawable.commondandelion,
        29 to R.drawable.cornpoppy,
        30 to R.drawable.cyclamen, // unzuverlässig // Gattung
        31 to R.drawable.daffodil, // Gattung
        32 to R.drawable.desertrose, // Gattung
        33 to R.drawable.englishmarigold, // funktioniert nur mit der orangenen gut
        34 to R.drawable.firelily, // unzuverlässig
        35 to R.drawable.foxglove, // etwas unzuverlässig // Gattung
        36 to R.drawable.frangipani,
        37 to R.drawable.fritillary, // Gattung
        38 to R.drawable.gardenphlox,
        39 to R.drawable.gaura,
        40 to R.drawable.gazania, // Eigentlich Gazania
        41 to R.drawable.geranium, // unzuverlässig // Gattung
        42 to R.drawable.giantwhitearumlily, //??
        43 to R.drawable.globeflower, // Gattung // unzuverlässig
        44 to R.drawable.globethistle,
        45 to R.drawable.grapehyacinth, // Gattung
        46 to R.drawable.greatmasterwort,
        47 to R.drawable.hardleavedpocketorchid, // etwas unzuverlässig
        48 to R.drawable.hibiscus, // Gattung
        49 to R.drawable.hippeastrum, // nur die pinke wird erkannt. es gibt verschiedene hippeastrum // Gattung
        50 to R.drawable.japaneseanemone, // nur die pinke funktioniert
        51 to R.drawable.kingprotea,
        52 to R.drawable.lentenrose, // unzuverlässig
        53 to R.drawable.lotus, // etwas unzuverlässig // Gattung
        54 to R.drawable.lotusinthemist,
        55 to R.drawable.magnolia, // funktioniert nicht // Gattung
        56 to R.drawable.mallow, // funktioniert nicht
        57 to R.drawable.marigold, // nur gelbe funktionieren
        58 to R.drawable.mexican_aster,
        59 to R.drawable.mexican_petunia,
        60 to R.drawable.monkshood, // etwas unzuverlässig // Gattung
        61 to R.drawable.moon_orchid,
        62 to R.drawable.morning_glory, // GROßE GATTUNG!!
        // 63 to orange dahlia find ich net
        64 to R.drawable.osteospermum, // es erkennt nur daisybush,eine Art von osteospermum // Gattung
        65 to R.drawable.oxeye_daisy,
        66 to R.drawable.passionflower, // Gattung
        67 to R.drawable.pelargonia, // Gattung
        // 68 to R.drawable.pervuvian lily, // Gattung
        69 to R.drawable.petunia, // Gattung
        70 to R.drawable.pincushionflower,
        // 71 to pinkyellowdahlia
        72 to R.drawable.pinkprimrose,
        73 to R.drawable.poinsettia,
        74 to R.drawable.primula, // Gattung
        75 to R.drawable.princeofwalesfeathres,
        76 to R.drawable.purpleconeflower,
        77 to R.drawable.alpiniapurupurata,
        78 to R.drawable.rose, // Gattung
        79 to R.drawable.rubylippedcattleya, // unzuverlässig
        80 to R.drawable.siamtulip,
        81 to R.drawable.silverbush,
        82 to R.drawable.snapdragon, // unzuverlässig Gattung
        83 to R.drawable.spearthistle,
        84 to R.drawable.springcrocus, // unzuverlässig
        85 to R.drawable.stemlessgentian,
        86 to R.drawable.sunflower,
        87 to R.drawable.sweetpea,
        88 to R.drawable.sweetwilliam,
        // 89 to R.drawable.swordlily // Gattung
        90 to R.drawable.thornapple, // Nur Blütenform funktioniert
        91 to R.drawable.tigerlily, // unzuverlässig
        92 to R.drawable.toadlily,
        93 to R.drawable.treemallow, // unzuverlässig
        94 to R.drawable.treepoppy,
        95 to R.drawable.trumpetcreeper,
        96 to R.drawable.wallflower, // unzuverlässig // Gattung
        97 to R.drawable.waterlily, //Familie -> Probleme
        98 to R.drawable.watercress, // funktioniert nicht
        99 to R.drawable.wildpansy,
        100 to R.drawable.windflower,
        101 to R.drawable.yellowiris
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
        visibil2.value = "gone"
        visibil3.value = "gone"
        visibil4.value = "gone"
        visibil5.value = "gone"
    }

    fun loadModule() {
        imageClassification = ImageClassification.create(
            //TODO Quantized und Float unterscheiden (über Option)
            // TODO Füge Option für Ändern des Devices (falls unterstützt) und Anzahl der Threads hinzu
            classifierModel = ClassifierModel.FLOAT,
            assetManager = getAsset(),
            modelPath = getPrefs().getString("modelFile", "model.tflite")!!,
            labelPath = LABELS_FILE_PATH_DE,
            confidenceThreshold = confidenceThreshold,
            numberOfResults = numResults,
            device = Device.CPU
        )
    }

    // TODO: mehrere Bilder klassifizieren und Durchschnitt berechnen
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
                part.postValue(i)
                imageView.postValue(imagePreview.get((results.await()[i].id).toInt()))
                if (i > 0) {
                    visibilList[i]!!.postValue("visible")
                }
            }
            done = true
        }
    }

    fun updateTrueDatabase(string: String) {
        val query = ParseQuery.getQuery<ParseObject>("Blumen")
        query.whereEqualTo("Blumenname", string)
        query.findInBackground { list, e ->
            if (e == null) {
                list[0].increment("AnzahlRichtig")
            } else {
                Log.d("DB-Error", "Error: " + e.message)
            }
        }
/*
        db.collection("blumen").document(string)
            .update("true", FieldValue.increment(1))
            .addOnSuccessListener {
                Log.d("DB-Update", "Successull update$string")
            }
            .addOnFailureListener {
                Log.e("DB-Update", "Error updating $string")
            }*/
    }

    fun updateFalseDatabase(string: String) {
        val query = ParseQuery.getQuery<ParseObject>("Blumen")
        query.whereEqualTo("Blumenname", string)
        query.findInBackground { list, e ->
            if (e == null) {
                list[0].increment("AnzahlFalsch")
            } else {
                Log.d("DB-Error", "Error: " + e.message)
            }
        }

/*        db.collection("blumen").document(string)
            .update("false", FieldValue.increment(1))
            .addOnSuccessListener {
                Log.d("DB-Update", "Successull update$string")
            }
            .addOnFailureListener {
                Log.e("DB-Update", "Error updating $string")
            }*/
    }
}
