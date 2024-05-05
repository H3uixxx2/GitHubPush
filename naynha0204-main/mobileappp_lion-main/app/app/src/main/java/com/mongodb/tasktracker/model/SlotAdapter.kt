package com.mongodb.tasktracker.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mongodb.tasktracker.R

class SlotAdapter(
    private var slots: List<SlotInfo>,
    private val onAttendButtonClickListener: OnAttendButtonClickListener
) : RecyclerView.Adapter<SlotAdapter.SlotViewHolder>() {

    interface OnAttendButtonClickListener {
        fun onAttendClick(slot: SlotInfo, position: Int)
    }

    class SlotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.sbname_data)
        private val dayTextView: TextView = view.findViewById(R.id.day_data)
        private val startTextView: TextView = view.findViewById(R.id.start_data)
        private val endTextView: TextView = view.findViewById(R.id.end_data)
        private val buildingTextView: TextView = view.findViewById(R.id.building_data)
        private val statusTextView: TextView = view.findViewById(R.id.Status_data)
        private val dateTextView: TextView = view.findViewById(R.id.date_data)
        private val attendButton: Button = view.findViewById(R.id.button_attend)

        fun bind(slot: SlotInfo, listener: OnAttendButtonClickListener) {
            titleTextView.text = slot.courseTitle
            dayTextView.text = slot.day
            startTextView.text = slot.startTime
            endTextView.text = slot.endTime
            buildingTextView.text = slot.building ?: "N/A"
            statusTextView.text = slot.attendanceStatus ?: "N/A"
            dateTextView.text = slot.attendanceDate ?: "N/A"

            attendButton.visibility = if (slot.attendanceStatus == "present") View.GONE else View.VISIBLE

            attendButton.setOnClickListener {
                listener.onAttendClick(slot, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attendance, parent, false)
        return SlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val slot = slots[position]
        holder.bind(slot, onAttendButtonClickListener)
    }

    override fun getItemCount(): Int = slots.size

    fun updateSlots(newSlots: List<SlotInfo>) {
        this.slots = newSlots
        notifyDataSetChanged()
    }
}
