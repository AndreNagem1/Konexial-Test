package com.example.konexialtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.status_item.view.*


class StatusListAdapter(private val listStatus: List<StatusChange>) :
    RecyclerView.Adapter<StatusListAdapter.StatusListViewHolder>() {
    private val mapHours = mapOf(
        0 to "12:00 AM",
        1 to "1:00 AM",
        2 to "2:00 AM",
        3 to "3:00 AM",
        4 to "4:00 AM",
        5 to "5:00 AM",
        6 to "6:00 AM",
        7 to "7:00 AM",
        8 to "8:00 AM",
        9 to "9:00 AM",
        10 to "10:00 AM",
        11 to "11:00 AM",
        12 to "12:00 PM",
        13 to "1:00 PM",
        14 to "2:00 PM",
        15 to "3:00 PM",
        16 to "4:00 PM",
        17 to "5:00 PM",
        18 to "6:00 PM",
        19 to "7:00 PM",
        20 to "8:00 PM",
        21 to "9:00 PM",
        22 to "10:00 PM",
        23 to "11:00 PM",
    )

    inner class StatusListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.status_item, parent, false)
        return StatusListViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusListViewHolder, position: Int) {
        holder.itemView.apply {
            itemStatus.text = listStatus[position].status.toString()
            itemTime.text = mapHours[listStatus[position].time.hour]
        }
    }

    override fun getItemCount() = listStatus.size
}