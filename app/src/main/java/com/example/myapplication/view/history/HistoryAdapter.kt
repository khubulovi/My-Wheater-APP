package com.example.myapplication.view.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Wheater

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() {
    private var wheater: List<Wheater> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(wheater: List<Wheater>) {
        this.wheater = wheater
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.history_adapter_rycyler_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(wheater[position])
    }

    override fun getItemCount(): Int {
        return wheater.size
    }

    class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(wheater: Wheater) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.apply {
                    findViewById<TextView>(R.id.itemOFHistory).text =
                        String.format("%s %d %d ", wheater.city.city, wheater.temperature, wheater.condition)
                    setOnClickListener {
                        Toast.makeText(
                            itemView.context,
                            "on click: ${wheater.city.city}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }
}
