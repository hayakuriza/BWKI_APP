package bwki.deepblossom.blumenidentifikator

/**
 * Datenklasse für die Results
 */

data class Result(
    val id: String,
    val name: String,
    val confidence: Float,
    val wissName: String
) {
    override fun toString(): String {
        var resultString = ""
        resultString += "[$id] "
        resultString += "$name "
        resultString += String.format("(%.1f%%) ", confidence * 100.0f)
        return resultString.trim { it <= ' ' }
    }
}