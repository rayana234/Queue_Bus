package com.example.myapplication1

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object SettingsManager {

    private const val PREF_NAME = "app_settings"
    private const val KEY_NOTIFICATIONS = "notifications_enabled"
    private const val KEY_LOCATION = "location_enabled"
    private const val KEY_DARK_MODE = "dark_mode_enabled"
    private const val KEY_LANGUAGE = "language"

    const val LANGUAGE_ENGLISH = "en"
    const val LANGUAGE_ARABIC = "ar"

    fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_NOTIFICATIONS, enabled)
            .apply()
    }

    fun isNotificationsEnabled(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_NOTIFICATIONS, true)
    }

    fun setLocationEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_LOCATION, enabled)
            .apply()
    }

    fun isLocationEnabled(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_LOCATION, true)
    }

    fun setDarkModeEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_DARK_MODE, enabled)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun isDarkModeEnabled(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_DARK_MODE, false)
    }

    fun setLanguage(context: Context, languageCode: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, languageCode)
            .apply()

        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(languageCode)
        )
    }

    fun getLanguage(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, LANGUAGE_ENGLISH) ?: LANGUAGE_ENGLISH
    }

    fun applySavedSettings(context: Context) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled(context)) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(getLanguage(context))
        )
    }
}