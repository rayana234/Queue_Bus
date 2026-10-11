package com.example.myapplication1

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.myapplication1.database.DatabaseHelper
import com.example.myapplication1.databinding.ActivityNotificationsBinding

class NotificationsActivity : BaseActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!SettingsManager.isNotificationsEnabled(this)) {
            Toast.makeText(
                this,
                getString(R.string.msg_notif_disabled),
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }

        databaseHelper = DatabaseHelper(this)

        if (databaseHelper.areAllNotificationsRead()) {
            hideNotifications()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnClearNotifications.setOnClickListener {
            databaseHelper.markAllNotificationsAsRead()
            hideNotifications()

            Toast.makeText(
                this,
                getString(R.string.all_notifications_read_toast),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun hideNotifications() {
        binding.cardBooking.visibility = View.GONE
        binding.cardArrival.visibility = View.GONE
        binding.cardDeparture.visibility = View.GONE
        binding.cardGeneral.visibility = View.GONE

        binding.txtSummary.text = getString(R.string.all_notifications_read)
        binding.btnClearNotifications.isEnabled = false
    }
}