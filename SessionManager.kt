package com.example.myapplication1

import android.content.Context
import com.example.myapplication1.database.DbContract

object SessionManager {

    private const val PREF_NAME = "user_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_TYPE = "user_type"

    fun saveSession(context: Context, userId: String, userType: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_USER_ID, userId)
            .putString(KEY_USER_TYPE, userType)
            .apply()
    }

    fun getUserId(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_ID, null)
    }

    fun getUserType(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_TYPE, DbContract.UserType.STUDENT)
            ?: DbContract.UserType.STUDENT
    }

    fun clearSession(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}