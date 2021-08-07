package com.example.tramtracker.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tramtracker.R
import javax.inject.Inject

class RouteDetailsAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: MutableList<String> = mutableListOf()
    private val ITEM_VIEW_TYPE_DIRECTION = 0
    private val ITEM_VIEW_TYPE_ARRIVAL_TIME = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE_DIRECTION) {
            DirectionViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.direction, parent, false),
                parent.context
            )
        } else {
            ArrivalTimeViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.route_arrival_time, parent, false),
                parent.context
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_VIEW_TYPE_DIRECTION) {
            with(holder as DirectionViewHolder) {
                holder.bind(items[position])
            }
        } else {
            with(holder as ArrivalTimeViewHolder) {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return if (items[position].contains("North") || items[position].contains("South"))
            ITEM_VIEW_TYPE_DIRECTION
        else
            ITEM_VIEW_TYPE_ARRIVAL_TIME
    }

    fun submitList(list: MutableList<String>) {
        items = list
        notifyDataSetChanged()
    }

    class DirectionViewHolder(
        itemView: View,
        private val parentContext: Context
    ) : RecyclerView.ViewHolder(itemView) {
        private val directionTextView: TextView = itemView.findViewById(R.id.direction)

        fun bind(direction: String) {
            directionTextView.text = direction
        }
    }

    class ArrivalTimeViewHolder(
        itemView: View,
        private val parentContext: Context
    ) : RecyclerView.ViewHolder(itemView) {
        private val arrivalTimeTextView: TextView = itemView.findViewById(R.id.arrival_time)

        fun bind(arrivalTime: String) {
            arrivalTimeTextView.text = arrivalTime
        }
    }
}