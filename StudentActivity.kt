package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication1.databinding.ActivityStudentBinding

class StudentActivity : BaseActivity() {

    private lateinit var binding: ActivityStudentBinding

    private var bookedSeats = 0
    private var totalSeats = 0
    private var hasBooked = false
    private var seatNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookedSeats = resources.getInteger(R.integer.initial_booked_seats)
        totalSeats = resources.getInteger(R.integer.total_seats)

        updateOccupancy()

        binding.btnConfirmBooking.isEnabled = false
        binding.btnCancelBooking.isEnabled = false
        binding.txtMessage.visibility = View.GONE

        binding.radioGroupDestination.setOnCheckedChangeListener { _, _ ->
            if (!hasBooked) {
                binding.btnConfirmBooking.isEnabled = true
            }
        }

        binding.btnConfirmBooking.setOnClickListener {
            confirmBooking()
        }

        binding.btnCancelBooking.setOnClickListener {
            cancelBooking()
        }

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnNotifications.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }
    }

    private fun confirmBooking() {
        val selectedId = binding.radioGroupDestination.checkedRadioButtonId

        if (selectedId == View.NO_ID) {
            binding.txtMessage.visibility = View.VISIBLE
            binding.txtMessage.text = getString(R.string.select_destination_first)
            return
        }

        if (hasBooked) {
            Toast.makeText(this, getString(R.string.already_have_booking), Toast.LENGTH_SHORT).show()
            return
        }

        if (bookedSeats >= totalSeats) {
            Toast.makeText(this, getString(R.string.bus_is_full), Toast.LENGTH_SHORT).show()
            return
        }

        val selectedDestination = findViewById<RadioButton>(selectedId).text.toString()

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_booking_dialog_title))
            .setMessage(getString(R.string.confirm_booking_dialog_message, selectedDestination))
            .setPositiveButton(getString(R.string.yes_confirm)) { _, _ ->
                bookedSeats++
                seatNumber = bookedSeats
                hasBooked = true

                updateOccupancy()

                binding.txtMessage.visibility = View.VISIBLE
                binding.txtMessage.text = getString(
                    R.string.booking_details_message,
                    seatNumber,
                    selectedDestination
                )

                binding.btnConfirmBooking.isEnabled = false
                binding.btnCancelBooking.isEnabled = true

                setDestinationEnabled(false)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun cancelBooking() {
        if (!hasBooked) return

        bookedSeats--
        hasBooked = false
        seatNumber = 0

        updateOccupancy()

        binding.txtMessage.visibility = View.VISIBLE
        binding.txtMessage.text = getString(R.string.booking_cancelled)

        binding.btnCancelBooking.isEnabled = false
        binding.btnConfirmBooking.isEnabled = false

        binding.radioGroupDestination.clearCheck()
        setDestinationEnabled(true)
    }

    private fun updateOccupancy() {
        binding.txtOccupancy.text = getString(R.string.occupancy_format, bookedSeats, totalSeats)
    }

    private fun setDestinationEnabled(enabled: Boolean) {
        for (i in 0 until binding.radioGroupDestination.childCount) {
            binding.radioGroupDestination.getChildAt(i).isEnabled = enabled
        }
    }
}