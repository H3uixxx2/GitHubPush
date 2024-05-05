package com.mongodb.tasktracker.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mongodb.tasktracker.R

class CourseAdapter(
    private var courses: List<CourseInfo>,
    private val context: Context,
    private val listener: OnCourseClickListener
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    interface OnCourseClickListener {
        fun onCourseClicked(instructor: IntructorInfo)
    }

    class CourseViewHolder(view: View, private val courses: List<CourseInfo>, private val listener: OnCourseClickListener) :
        RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.TT_data)
        val departmentTextView: TextView = view.findViewById(R.id.Depart_data)
        val descriptionTextView: TextView = view.findViewById(R.id.Desc_data)
        val creditTextView: TextView = view.findViewById(R.id.credit_data)

        init {
            view.setOnClickListener {
                val course = courses[adapterPosition]
                if (course.instructors.isNotEmpty()) {
                    listener.onCourseClicked(course.instructors.first())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view, courses, listener)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.titleTextView.text = course.title
        holder.departmentTextView.text = course.departmentName
        holder.descriptionTextView.text = course.description
        holder.creditTextView.text = course.credits.toString()
    }

    override fun getItemCount() = courses.size

    fun updateCourses(newCourses: List<CourseInfo>) {
        courses = newCourses
        notifyDataSetChanged()
    }
}
