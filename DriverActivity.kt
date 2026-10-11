package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication1.databinding.ActivityDriverBinding

class DriverActivity : BaseActivity() {

    private lateinit var binding: ActivityDriverBinding

    private var bookedSeats = 0
    private var totalSeats = 0

    private data class StudentItem(val name: String, val id: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookedSeats = resources.getInteger(R.integer.initial_booked_seats)
        totalSeats = resources.getInteger(R.integer.total_seats)

        updateOccupancy()
        loadStudents()
        setupActions()
    }

    private fun setupActions() {
        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnAddSeat.setOnClickListener {
            if (bookedSeats < totalSeats) {
                bookedSeats++
                updateOccupancy()
            } else {
                Toast.makeText(this, getString(R.string.bus_is_full), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRemoveSeat.setOnClickListener {
            if (bookedSeats > 0) {
                bookedSeats--
                updateOccupancy()
            } else {
                Toast.makeText(this, getString(R.string.no_booked_seats), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnArrive.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_arrived), Toast.LENGTH_SHORT).show()
        }

        binding.btnDepart.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_departed), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateOccupancy() {
        binding.txtOccupancy.text = getString(R.string.occupancy_format, bookedSeats, totalSeats)
    }

    private fun loadStudents() {
        binding.studentsContainer.removeAllViews()

        val studentsList = listOf(
            StudentItem(getString(R.string.student_1_name), getString(R.string.student_1_id)),
            StudentItem(getString(R.string.student_2_name), getString(R.string.student_2_id)),
            StudentItem(getString(R.string.student_3_name), getString(R.string.student_3_id))
        )

        val paddingInPx = resources.getDimensionPixelSize(R.dimen.padding_small)
        val textSizeInPx = resources.getDimension(R.dimen.text_size_message)

        for (student in studentsList) {
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            row.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)

            val checkBox = CheckBox(this)

            val text = TextView(this)
            text.text = getString(R.string.student_format, student.name, student.id)
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeInPx)
            text.setPadding(paddingInPx, 0, 0, 0)

            row.addView(checkBox)
            row.addView(text)

            binding.studentsContainer.addView(row)
        }
    }
}