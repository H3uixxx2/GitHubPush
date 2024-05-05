package com.mongodb.tasktracker

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.mongodb.tasktracker.model.CourseAdapter
import com.mongodb.tasktracker.model.CourseInfo
import com.mongodb.tasktracker.model.IntructorInfo

class infoFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_infor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Thiết lập thông tin cá nhân
        val nameTextView = view.findViewById<TextView>(R.id.name_data)
        val emailTextView = view.findViewById<TextView>(R.id.email_data)
        val departmentTextView = view.findViewById<TextView>(R.id.department_data)
        /*val ppTextView = view.findViewById<TextView>(R.id.pp_number)
        val ldtsTextView = view.findViewById<TextView>(R.id.lDTs_number)*/

        val name = arguments?.getString("name", "N/A")
        val email = arguments?.getString("email", "N/A")
        val department = arguments?.getString("department", "N/A")
        /*val pp = arguments?.getDouble("PP", 0.0)
        val ldts = arguments?.getDouble("LDTs", 0.0)*/

        nameTextView.text = name ?: "N/A"
        emailTextView.text = email ?: "N/A"
        departmentTextView.text = department ?: "N/A"
        /*ppTextView.text = pp?.toString() ?: "0.0"
        ldtsTextView.text = ldts?.toString() ?: "0.0"*/

        // Thiết lập RecyclerView
        recyclerView = view.findViewById(R.id.my_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        courseAdapter = CourseAdapter(emptyList(), requireContext(), object : CourseAdapter.OnCourseClickListener {
            override fun onCourseClicked(instructor: IntructorInfo) {
                showInstructorInfoDialog(instructor)
            }
        })
        recyclerView.adapter = courseAdapter

        // Lấy dữ liệu khóa học từ arguments và cập nhật RecyclerView
        val coursesInfo = arguments?.getSerializable("courses") as? ArrayList<CourseInfo>
        coursesInfo?.let {
            courseAdapter.updateCourses(it)
        }
    }

    private fun showInstructorInfoDialog(instructor: IntructorInfo) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.instructor_info_dialog, null)
        val instructorNameTextView = dialogView.findViewById<TextView>(R.id.instructor_name)
        val instructorEmailTextView = dialogView.findViewById<TextView>(R.id.instructor_email)

        instructorNameTextView.text = instructor.teacherName
        instructorEmailTextView.text = instructor.teacherEmail
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Your Teacher")
            .setView(dialogView)
            .create()

        dialog.show()
    }
}
