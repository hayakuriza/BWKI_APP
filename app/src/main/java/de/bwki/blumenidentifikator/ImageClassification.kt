package de.bwki.blumenidentifikator

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.os.SystemClock
import android.os.Trace
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import java.nio.ByteBuffer
import java.nio.ByteOrder.nativeOrder


//TODO Modeltype anpassen
enum class ClassifierModel {
    FLOAT,
    QUANTIZED
}

enum class Device {
    CPU,
    NNAPI,
    GPU
}

abstract class ImageClassification protected constructor(
    val interpreter: Interpreter,
    val labelList: List<String>,
    val inputSize: Int,
    val numberOfResults: Int,
    val confidenceThreshold: Float
) {
    protected val imageByteBuffer: ByteBuffer by lazy {
        ByteBuffer.allocateDirect(byteNumbersPerChannel() * BATCH_SIZE * inputSize * inputSize * PIXEL_SIZE)
            .order(nativeOrder())
    }

    //TODO Typ anpassen?
    fun classifyImage(bitmap: Bitmap): List<Recognizable> {
        Trace.beginSection("recognizeImage")

        Trace.beginSection("convertBitmap")
        convertBitmapToByteBuffer(bitmap)
        Trace.endSection()

        // Run Interpreter
        Trace.beginSection("runInterpreter")
        val startTime: Long = SystemClock.uptimeMillis()
        runInterpreter()
        val endTime: Long = SystemClock.uptimeMillis()
        Trace.endSection()
        Log.v("Model", "Timecost for Classification:" + (endTime - startTime))
        return getResult()
    }

    //Close interpreter
    fun close() {
        if (gpuDelegate != null) {
            gpuDelegate?.close()
            gpuDelegate = null
        }
        interpreter.close()
    }

    /**
     * The number of bytes that is used to store a single color.
     *
     * In case of [ClassifierModel.QUANTIZED], the result is 1.
     * [ClassifierModel.FLOAT], the result is 4.
     */
    protected abstract fun byteNumbersPerChannel(): Int

    // Add pixel value to the byte buffer
    protected abstract fun addPixelValueToBuffer(pixelValue: Int)

    //Gets the normalized probability for the indexed label, this will represent the confidence
    protected abstract fun normalizedProbability(labelIndex: Int): Float

    protected abstract fun runInterpreter()

    private fun convertBitmapToByteBuffer(bitmap: Bitmap) {
        imageByteBuffer.rewind()

        val emptyIntArray = IntArray(inputSize * inputSize)
        bitmap.getPixels(emptyIntArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (x in 0 until inputSize) {
            for (y in 0 until inputSize) {
                val pixelValue = emptyIntArray[pixel++]
                addPixelValueToBuffer(pixelValue)
            }
        }
    }

    private fun getResult(): List<Recognizable> {
        val priorityQueue = priorityQueue<Recognizable>(
            numberOfResults,
            Comparator { o1, o2 ->
                o2.confidence.compareTo(o1.confidence)
            })

        labelList.forEachIndexed { index, label ->
            val confidence = normalizedProbability(index)
            if (confidence > confidenceThreshold) {
                priorityQueue.add(
                    Recognizable(
                        id = index.toString(),
                        name = if (labelList.size > index) label else "unknown",
                        confidence = confidence
                    )
                )
            }
        }

        return priorityQueue.take(numberOfResults)
    }


    companion object {
        /**
         * Factory method, which returns [ImageClassification] based on the model type [ClassifierModel].
         *
         * [assetManager] provides access to an application's raw asset files.
         * [modelPath] the path name of the model.
         * [labelList] the labels list name.
         * [inputSize] the size that is used in the model.
         * [interpreterOptions] the options that will be used by [Interpreter].
         * [numberOfResults] the number of results to show.
         * [confidenceThreshold] the threshold confidence, [classifyImage] will return results whose confidence more than this value.
         * [device] what device config to be used
         * [numThreads] number of threads to be sued
         *
         */
        var gpuDelegate: GpuDelegate? = null
        fun create(
            classifierModel: ClassifierModel,
            assetManager: AssetManager,
            modelPath: String = MODEL_FILE_PATH,
            labelPath: String = LABELS_FILE_PATH,
            inputSize: Int = DEFAULT_INPUT_SIZE,
            interpreterOptions: Interpreter.Options = Interpreter.Options(),
            numberOfResults: Int = DEFAULT_MAX_RESULTS,
            confidenceThreshold: Float = DEFAULT_CONFIDENCE_THRESHOLD,
            device: Device = Device.CPU,
            numThreads: Int = NUM_THREADS
        ): ImageClassification {

            when (device) {
                Device.NNAPI -> interpreterOptions.setUseNNAPI(true)
                Device.GPU -> {
                    gpuDelegate = GpuDelegate()
                    interpreterOptions.addDelegate(gpuDelegate)
                }
                Device.CPU -> {
                }
            }
            interpreterOptions.setNumThreads(numThreads)

            val interpreter = Interpreter(
                assetManager.loadModelFile(modelPath),
                interpreterOptions
            )

            Log.d(
                "TFModel",
                "Created Classifier with" + device.toString() + "Type:" + classifierModel.toString()
            )

            return when (classifierModel) {
                ClassifierModel.QUANTIZED -> QuantizedClassifier(
                    interpreter = interpreter,
                    labelList = assetManager.loadLabelList(labelPath),
                    inputSize = inputSize,
                    numberOfResults = numberOfResults,
                    confidenceThreshold = confidenceThreshold
                )
                ClassifierModel.FLOAT -> FloatClassifier(
                    interpreter = interpreter,
                    labelList = assetManager.loadLabelList(labelPath),
                    inputSize = inputSize,
                    numberOfResults = numberOfResults,
                    confidenceThreshold = confidenceThreshold
                )
            }
        }
    }
}

/**
 * A concrete implementation of [ImageClassification] of type [ClassifierModel.QUANTIZED].
 */
private class QuantizedClassifier(
    interpreter: Interpreter,
    labelList: List<String>,
    inputSize: Int,
    numberOfResults: Int,
    confidenceThreshold: Float
) : ImageClassification(interpreter, labelList, inputSize, numberOfResults, confidenceThreshold) {

    private val labelResults = Array(1) { ByteArray(labelList.size) }

    override fun byteNumbersPerChannel(): Int {
        return 1
    }

    override fun addPixelValueToBuffer(pixelValue: Int) {
        imageByteBuffer.put(((pixelValue shr 16) and 0xFF).toByte())
        imageByteBuffer.put(((pixelValue shr 8) and 0xFF).toByte())
        imageByteBuffer.put((pixelValue and 0xFF).toByte())
    }

    override fun normalizedProbability(labelIndex: Int): Float {
        return (labelResults[0][labelIndex].toInt() and 0xff) / 255.0f
    }

    override fun runInterpreter() {
        interpreter.run(imageByteBuffer, labelResults)
    }
}

/**
 * A concrete implementation of [ImageClassification] of type [ClassifierModel.FLOAT].
 */
private class FloatClassifier(
    interpreter: Interpreter,
    labelList: List<String>,
    inputSize: Int,
    numberOfResults: Int,
    confidenceThreshold: Float
) : ImageClassification(interpreter, labelList, inputSize, numberOfResults, confidenceThreshold) {

    private val labelResults = Array(1) { FloatArray(labelList.size) }

    override fun byteNumbersPerChannel(): Int {
        return 4
    }

    override fun addPixelValueToBuffer(pixelValue: Int) {
        imageByteBuffer.putFloat(((pixelValue shr 16) and 0xFF) / 255f)
        imageByteBuffer.putFloat(((pixelValue shr 8) and 0xFF) / 255f)
        imageByteBuffer.putFloat((pixelValue and 0xFF) / 255f)
    }

    override fun normalizedProbability(labelIndex: Int): Float {
        return labelResults[0][labelIndex]
    }

    override fun runInterpreter() {
        interpreter.run(imageByteBuffer, labelResults)
    }
}
