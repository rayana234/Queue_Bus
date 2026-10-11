package com.example.myapplication1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        SettingsManager.applySavedSettings(this)
        super.onCreate(savedInstanceState)
    }
}