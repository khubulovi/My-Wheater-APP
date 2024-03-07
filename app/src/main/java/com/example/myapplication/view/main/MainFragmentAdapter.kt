package com.example.myapplication.view.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Wheater

class MainFragmentAdapter(
    private var onItemViewClickListener:
    MainActivityFragment.OnItemViewClickListener?
) : RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {
    private var wheaterData: List<Wheater> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setWheater(data: List<Wheater>) {
        wheaterData = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.main_fragment_recycler_item, parent, false
            ) as View
        )
    }

    override fun getItemCount(): Int = wheaterData.size


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(wheaterData[position])
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(wheater: Wheater) {
            itemView.apply {
                findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text =
                    wheater.city.city
                setOnClickListener {
                    onItemViewClickListener?.itemViewClickListener(wheater)
                }
            }
        }
    }
}