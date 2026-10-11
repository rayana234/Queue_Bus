package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication1.database.DatabaseHelper
import com.example.myapplication1.database.DbContract
import com.example.myapplication1.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var selectedUserType = DbContract.UserType.STUDENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        updateUserTypeButtons()

        binding.btnStudent.setOnClickListener {
            selectedUserType = DbContract.UserType.STUDENT
            updateUserTypeButtons()
            Toast.makeText(this, getString(R.string.student_selected), Toast.LENGTH_SHORT).show()
        }

        binding.btnDriver.setOnClickListener {
            selectedUserType = DbContract.UserType.DRIVER
            updateUserTypeButtons()
            Toast.makeText(this, getString(R.string.driver_selected), Toast.LENGTH_SHORT).show()
        }

        binding.btnCreate.setOnClickListener {
            createAccount()
        }
    }

    private fun updateUserTypeButtons() {
        binding.btnStudent.isSelected = selectedUserType == DbContract.UserType.STUDENT
        binding.btnDriver.isSelected = selectedUserType == DbContract.UserType.DRIVER
    }

    private fun createAccount() {
        val name = binding.etName.text.toString().trim()
        val id = binding.etStudentId.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (name.isEmpty() || id.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.please_fill_all_fields),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (databaseHelper.isUserExists(id)) {
            Toast.makeText(
                this,
                getString(R.string.id_already_exists),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val inserted = databaseHelper.insertUser(
            fullName = name,
            universityId = id,
            password = password,
            userType = selectedUserType
        )

        if (inserted) {
            Toast.makeText(
                this,
                getString(R.string.account_created_successfully),
                Toast.LENGTH_SHORT
            ).show()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            Toast.makeText(
                this,
                getString(R.string.account_create_failed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}