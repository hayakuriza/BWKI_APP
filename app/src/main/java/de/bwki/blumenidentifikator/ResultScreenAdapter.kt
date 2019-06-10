package de.bwki.blumenidentifikator

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ResultScreenAdapter : RecyclerView.Adapter<TextItemViewHolder>() {
    var data = listOf<Recognizable>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = DEFAULT_MAX_RESULTS

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }
}