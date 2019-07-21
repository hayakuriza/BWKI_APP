package de.bwki.blumenidentifikator

const val MODEL_FILE_PATH = "model.tflite"
const val Q_MODEL_FILE_PATH = "q_model.tflite"
const val LABELS_FILE_PATH = "labels.txt"
const val LABELS_FILE_PATH_DE = "labelsDeu.txt"
const val LABELS_FILE_PATH_WISS = "labelsWiss.txt"
const val MODEL_INPUT_NAME = "input"
const val MODEL_OUTPUT_NAME = "final_result"


//TODO: Anpassen
const val DEFAULT_MAX_RESULTS = 3
const val DEFAULT_CONFIDENCE_THRESHOLD = 0.1f
const val DEFAULT_INPUT_SIZE = 224
const val BATCH_SIZE = 1
const val PIXEL_SIZE = 3
const val NUM_THREADS = 2
