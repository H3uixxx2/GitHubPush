package com.mongodb.tasktracker

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import org.bson.Document
import at.favre.lib.crypto.bcrypt.BCrypt
import org.bson.AbstractBsonWriter


class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var app: App
    private lateinit var rememberMeCheckBox: CheckBox

    private val prefsName = "Final_Project"
    private val usernameKey = "username"
    private val passwordKey = "password"
    private val rememberMeKey = "rememberMe"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Started Realm
        Realm.init(this)
        val appConfiguration = AppConfiguration.Builder("finalproject-rujev").build()
        app = App(appConfiguration)

        // Move this code into loginAsync
        app.loginAsync(Credentials.emailPassword("mobile@gmail.com", "123123")) { result ->
            if (result.isSuccess) {
                Log.v("User", "Login Successful!")

                fetchData()
            } else {
                Log.e("User", "Login Failed: ${result.error}")
            }
        }

        usernameEditText = findViewById(R.id.input_username)
        passwordEditText = findViewById(R.id.input_password)
        loginButton = findViewById(R.id.button_login)
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        checkSavedCredentials()

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            Login(username, password)
        }
    }

    private fun fetchData() {
        val user = app.currentUser()
        val mongoClient = user!!.getMongoClient("mongodb-atlas")
        val database = mongoClient.getDatabase("finalProject")
        val collection = database.getCollection("Users")

        collection.find().iterator().getAsync { task ->
            if (task.isSuccess) {
                val results = task.get()
                while (results.hasNext()) {
                    val document = results.next()
                    Log.v("Data", "Find document: ${document.toJson()}")
                }
            } else {
                Log.e("Data", "Error to fetch data: ${task.error}")
            }
        }
    }

    //Check app remember
    private fun checkSavedCredentials() {
        val settings = getSharedPreferences(prefsName, MODE_PRIVATE)
        val savedUsername = settings.getString(usernameKey, "")
        val savedPassword = settings.getString(passwordKey, "")
        val rememberMe = settings.getBoolean(rememberMeKey, false)

        if (rememberMe && !savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            usernameEditText.setText(savedUsername)
            passwordEditText.setText(savedPassword)
            rememberMeCheckBox.isChecked = true
        }
    }

    private fun Login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password must not be empty", Toast.LENGTH_LONG).show()
            return
        }

        val mongoClient = app.currentUser()!!.getMongoClient("mongodb-atlas")
        val database = mongoClient.getDatabase("finalProject")
        val collection = database.getCollection("Users")

        // Create query with email and role
        val query = Document("details.email", email)

        collection.findOne(query).getAsync { task ->
            if (task.isSuccess) {
                val userDocument = task.get()
                if (userDocument != null) {
                    val role = userDocument.getString("role")
                    // check roles is "student"
                    if (role == "student") {
                        val storedPasswordHash = userDocument.getString("password")
                        val result = BCrypt.verifyer().verify(password.toCharArray(), storedPasswordHash)
                        if (result.verified) {
                            Log.v("Login", "Login Successful!")
                            rememberAccount(email, password, rememberMeCheckBox.isChecked)

                            val homeIntent = Intent(this, HomeActivity::class.java).apply {
                                putExtra("USER_EMAIL", email)
                                putExtra("SHOW_INFOR_FRAGMENT", false)
                            }
                            startActivity(homeIntent)
                            finish()
                        } else {
                            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Access denied: User is not a student", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Error during login: ${task.error.errorMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun rememberAccount(username: String, password: String, rememberMe: Boolean) {
        val settings = getSharedPreferences(prefsName, MODE_PRIVATE)
        settings.edit().apply {
            if (rememberMe) {
                putString(usernameKey, username)
                putString(passwordKey, password)
            } else {
                remove(usernameKey)
                remove(passwordKey)
            }
            putBoolean(rememberMeKey, rememberMe)
            apply()
        }
    }
}
