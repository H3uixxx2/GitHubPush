package com.mongodb.tasktracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mongodb.tasktracker.R
import com.mongodb.tasktracker.model.AttendanceRecord
import java.text.SimpleDateFormat
import java.util.Locale

class AttendanceAdapter(private var attendanceRecords: List<AttendanceRecord>) : RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    class AttendanceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val courseTitleView: TextView = view.findViewById(R.id.courseTitle)
        private val attendanceDateView: TextView = view.findViewById(R.id.attendanceDate)
        private val attendanceStatusView: TextView = view.findViewById(R.id.attendanceStatus)

        fun bind(record: AttendanceRecord) {
            courseTitleView.text = record.courseTitle
            attendanceDateView.text = record.attendanceDate
            attendanceStatusView.text = record.attendanceStatus
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attendance_record, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(attendanceRecords[position])
    }

    override fun getItemCount(): Int = attendanceRecords.size

    fun updateRecords(newRecords: List<AttendanceRecord>) {
        attendanceRecords = newRecords
        notifyDataSetChanged()
    }
}
