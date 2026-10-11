package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication1.database.DatabaseHelper
import com.example.myapplication1.database.DbContract
import com.example.myapplication1.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.btnLogin.setOnClickListener {
            loginUser()
        }

        binding.tvCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun loginUser() {
        val enteredId = binding.etStudentId.text.toString().trim()
        val enteredPassword = binding.etPassword.text.toString().trim()

        if (enteredId.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.please_enter_id_password),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val userType = databaseHelper.loginUser(enteredId, enteredPassword)

        if (userType == null) {
            Toast.makeText(
                this,
                getString(R.string.invalid_login),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        SessionManager.saveSession(this, enteredId, userType)

        if (userType == DbContract.UserType.DRIVER) {
            startActivity(Intent(this, DriverActivity::class.java))
        } else {
            startActivity(Intent(this, StudentActivity::class.java))
        }

        finish()
    }
}