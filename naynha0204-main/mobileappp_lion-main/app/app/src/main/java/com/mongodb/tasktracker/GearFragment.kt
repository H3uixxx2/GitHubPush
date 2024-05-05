package com.mongodb.tasktracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class GearFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gear, container, false)

        val logoutButton = view.findViewById<Button>(R.id.LogoutButton)
        logoutButton.setOnClickListener {
            logout()
        }

        return view
    }

    private fun logout() {
        val mainActivity = activity as? HomeActivity
        mainActivity?.app?.currentUser()?.logOutAsync { result ->
            if (result.isSuccess) {
                // Intent to restart LoginActivity
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK  // Clear the activity stack
                startActivity(intent)
            } else {
                result.error.printStackTrace()  // In lỗi nếu đăng xuất không thành công
            }
        }
    }

}

