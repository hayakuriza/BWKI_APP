package de.bwki.blumenidentifikator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.util.StringUtil
import kotlinx.coroutines.*

class ResultScreenModel: ViewModel(), MainActivity.GlobalMethods{
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var resultList = MutableLiveData<Array<String>>()
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

}
