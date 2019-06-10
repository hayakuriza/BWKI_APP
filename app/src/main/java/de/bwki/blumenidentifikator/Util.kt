package de.bwki.blumenidentifikator

/*
 * Copyright 2019 Ahmed Gamal. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.*
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.PriorityQueue
import kotlin.collections.ArrayList

fun <T> priorityQueue(capacity: Int, block: (Comparator<T>)) =
    PriorityQueue<T>(capacity, block)

fun ByteArray.toBitmap(): Bitmap {
    val byteArrayInputString = ByteArrayInputStream(this)
    return BitmapFactory.decodeStream(byteArrayInputString)
}

@Throws(IOException::class)
fun AssetManager.loadModelFile(modelPath: String): MappedByteBuffer {
    val fileDescriptor = this.openFd(modelPath)
    val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}

@Throws(IOException::class)
fun AssetManager.loadLabelList(labelPath: String): List<String> {
    val labelList = ArrayList<String>()
    val reader = BufferedReader(InputStreamReader(this.open(labelPath)))
    reader.use {
        while (true) {
            val line = reader.readLine() ?: break
            labelList.add(line)
        }
    }
    return labelList
}

class TextItemViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)