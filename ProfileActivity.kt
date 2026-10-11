package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.myapplication1.database.DatabaseHelper
import com.example.myapplication1.database.DbContract
import com.example.myapplication1.databinding.ActivityProfileBinding

class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        loadUserData()
        setupClickListeners()
    }

    private fun loadUserData() {
        val userId = SessionManager.getUserId(this)
        val user = userId?.let { databaseHelper.getUserById(it) }

        if (user == null) {
            loadDefaultStudentProfile()
            return
        }

        binding.txtProfileName.text = user.fullName
        binding.txtUserId.text = getString(R.string.profile_id_format, user.universityId)

        if (user.userType == DbContract.UserType.DRIVER) {
            loadDriverProfile()
        } else {
            loadStudentProfile()
        }
    }

    private fun loadStudentProfile() {
        binding.txtAccountType.text = getString(R.string.student_account)
        binding.txtDepartment.text = getString(R.string.student_department_value)
        binding.txtPhone.text = getString(R.string.student_phone_value)
        binding.txtEmail.text = getString(R.string.student_email_value)
        binding.txtTripsCount.text = getString(R.string.student_trips_count)
        binding.txtCurrentStatus.text = getString(R.string.student_status_value)
        binding.txtCurrentBookingTitle.text = getString(R.string.current_booking)
        binding.txtBookingInfo.text = getString(R.string.student_booking_info)
    }

    private fun loadDriverProfile() {
        binding.txtAccountType.text = getString(R.string.driver_account)
        binding.txtDepartment.text = getString(R.string.driver_bus_value)
        binding.txtPhone.text = getString(R.string.driver_phone_value)
        binding.txtEmail.text = getString(R.string.driver_email_value)
        binding.txtTripsCount.text = getString(R.string.driver_trips_count)
        binding.txtCurrentStatus.text = getString(R.string.driver_status_value)
        binding.txtCurrentBookingTitle.text = getString(R.string.current_work_shift)
        binding.txtBookingInfo.text = getString(R.string.driver_shift_info)
    }

    private fun loadDefaultStudentProfile() {
        binding.txtProfileName.text = getString(R.string.student_name_value)
        binding.txtUserId.text = getString(R.string.student_id_value)
        loadStudentProfile()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnGoToMain.setOnClickListener {
            if (SessionManager.getUserType(this) == DbContract.UserType.DRIVER) {
                startActivity(Intent(this, DriverActivity::class.java))
            } else {
                startActivity(Intent(this, StudentActivity::class.java))
            }
            finish()
        }

        binding.btnOpenSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.logout_dialog_title))
                .setMessage(getString(R.string.logout_dialog_message))
                .setPositiveButton(getString(R.string.logout)) { _, _ ->
                    SessionManager.clearSession(this)

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }
    }
}