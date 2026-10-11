package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication1.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadSavedSettings()
        setupClickListeners()
    }

    private fun loadSavedSettings() {
        binding.switchNotifications.isChecked = SettingsManager.isNotificationsEnabled(this)
        binding.switchLocation.isChecked = SettingsManager.isLocationEnabled(this)
        binding.switchDarkMode.isChecked = SettingsManager.isDarkModeEnabled(this)
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.setNotificationsEnabled(this, isChecked)

            val message = if (isChecked) {
                getString(R.string.msg_notif_enabled)
            } else {
                getString(R.string.msg_notif_disabled)
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.setLocationEnabled(this, isChecked)

            val message = if (isChecked) {
                getString(R.string.msg_loc_enabled)
            } else {
                getString(R.string.msg_loc_disabled)
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.setDarkModeEnabled(this, isChecked)

            val message = if (isChecked) {
                getString(R.string.msg_dark_enabled)
            } else {
                getString(R.string.msg_dark_disabled)
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            recreate()
        }

        binding.btnEnglish.setOnClickListener {
            SettingsManager.setLanguage(this, SettingsManager.LANGUAGE_ENGLISH)
            Toast.makeText(this, getString(R.string.msg_lang_english), Toast.LENGTH_SHORT).show()
            recreate()
        }

        binding.btnArabic.setOnClickListener {
            SettingsManager.setLanguage(this, SettingsManager.LANGUAGE_ARABIC)
            Toast.makeText(this, getString(R.string.msg_lang_arabic), Toast.LENGTH_SHORT).show()
            recreate()
        }

        binding.txtOpenProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
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