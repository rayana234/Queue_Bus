package com.example.myapplication1.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication1.R

class DatabaseHelper(private val appContext: Context) : SQLiteOpenHelper(
    appContext,
    DbContract.DATABASE_NAME,
    null,
    DbContract.DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        createUsersTable(db)
        createNotificationsTable(db)
        insertDefaultNotifications(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${DbContract.Users.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DbContract.Notifications.TABLE_NAME}")
        onCreate(db)
    }

    private fun createUsersTable(db: SQLiteDatabase) {
        val query = """
            CREATE TABLE ${DbContract.Users.TABLE_NAME} (
                ${DbContract.Users.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${DbContract.Users.FULL_NAME} TEXT NOT NULL,
                ${DbContract.Users.UNIVERSITY_ID} TEXT NOT NULL UNIQUE,
                ${DbContract.Users.PASSWORD} TEXT NOT NULL,
                ${DbContract.Users.USER_TYPE} TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(query)
    }

    private fun createNotificationsTable(db: SQLiteDatabase) {
        val query = """
            CREATE TABLE ${DbContract.Notifications.TABLE_NAME} (
                ${DbContract.Notifications.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${DbContract.Notifications.TITLE} TEXT NOT NULL,
                ${DbContract.Notifications.MESSAGE} TEXT NOT NULL,
                ${DbContract.Notifications.TIME} TEXT NOT NULL,
                ${DbContract.Notifications.IS_READ} INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent()

        db.execSQL(query)
    }

    private fun insertDefaultNotifications(db: SQLiteDatabase) {
        insertNotification(
            db,
            appContext.getString(R.string.booking_confirmed_title),
            appContext.getString(R.string.booking_confirmed_body),
            appContext.getString(R.string.booking_notification_time)
        )

        insertNotification(
            db,
            appContext.getString(R.string.bus_arriving_title),
            appContext.getString(R.string.bus_arriving_body),
            appContext.getString(R.string.bus_arriving_time)
        )

        insertNotification(
            db,
            appContext.getString(R.string.departure_reminder_title),
            appContext.getString(R.string.departure_reminder_body),
            appContext.getString(R.string.departure_reminder_time)
        )

        insertNotification(
            db,
            appContext.getString(R.string.new_trips_title),
            appContext.getString(R.string.new_trips_body),
            appContext.getString(R.string.new_trips_time)
        )
    }

    private fun insertNotification(
        db: SQLiteDatabase,
        title: String,
        message: String,
        time: String
    ) {
        val values = ContentValues().apply {
            put(DbContract.Notifications.TITLE, title)
            put(DbContract.Notifications.MESSAGE, message)
            put(DbContract.Notifications.TIME, time)
            put(DbContract.Notifications.IS_READ, 0)
        }

        db.insert(DbContract.Notifications.TABLE_NAME, null, values)
    }

    fun insertUser(
        fullName: String,
        universityId: String,
        password: String,
        userType: String
    ): Boolean {
        val values = ContentValues().apply {
            put(DbContract.Users.FULL_NAME, fullName)
            put(DbContract.Users.UNIVERSITY_ID, universityId)
            put(DbContract.Users.PASSWORD, password)
            put(DbContract.Users.USER_TYPE, userType)
        }

        val result = writableDatabase.insert(
            DbContract.Users.TABLE_NAME,
            null,
            values
        )

        return result != -1L
    }

    fun isUserExists(universityId: String): Boolean {
        val cursor = readableDatabase.rawQuery(
            """
                SELECT ${DbContract.Users.ID}
                FROM ${DbContract.Users.TABLE_NAME}
                WHERE ${DbContract.Users.UNIVERSITY_ID} = ?
            """.trimIndent(),
            arrayOf(universityId)
        )

        val exists = cursor.moveToFirst()
        cursor.close()

        return exists
    }

    fun loginUser(universityId: String, password: String): String? {
        val cursor = readableDatabase.rawQuery(
            """
                SELECT ${DbContract.Users.USER_TYPE}
                FROM ${DbContract.Users.TABLE_NAME}
                WHERE ${DbContract.Users.UNIVERSITY_ID} = ?
                AND ${DbContract.Users.PASSWORD} = ?
            """.trimIndent(),
            arrayOf(universityId, password)
        )

        var userType: String? = null

        if (cursor.moveToFirst()) {
            userType = cursor.getString(
                cursor.getColumnIndexOrThrow(DbContract.Users.USER_TYPE)
            )
        }

        cursor.close()
        return userType
    }

    fun getUserById(universityId: String): User? {
        val cursor = readableDatabase.rawQuery(
            """
                SELECT ${DbContract.Users.FULL_NAME},
                       ${DbContract.Users.UNIVERSITY_ID},
                       ${DbContract.Users.USER_TYPE}
                FROM ${DbContract.Users.TABLE_NAME}
                WHERE ${DbContract.Users.UNIVERSITY_ID} = ?
            """.trimIndent(),
            arrayOf(universityId)
        )

        var user: User? = null

        if (cursor.moveToFirst()) {
            user = User(
                fullName = cursor.getString(
                    cursor.getColumnIndexOrThrow(DbContract.Users.FULL_NAME)
                ),
                universityId = cursor.getString(
                    cursor.getColumnIndexOrThrow(DbContract.Users.UNIVERSITY_ID)
                ),
                userType = cursor.getString(
                    cursor.getColumnIndexOrThrow(DbContract.Users.USER_TYPE)
                )
            )
        }

        cursor.close()
        return user
    }

    fun markAllNotificationsAsRead() {
        val values = ContentValues().apply {
            put(DbContract.Notifications.IS_READ, 1)
        }

        writableDatabase.update(
            DbContract.Notifications.TABLE_NAME,
            values,
            null,
            null
        )
    }

    fun areAllNotificationsRead(): Boolean {
        val cursor = readableDatabase.rawQuery(
            """
                SELECT ${DbContract.Notifications.ID}
                FROM ${DbContract.Notifications.TABLE_NAME}
                WHERE ${DbContract.Notifications.IS_READ} = 0
            """.trimIndent(),
            null
        )

        val allRead = !cursor.moveToFirst()
        cursor.close()

        return allRead
    }
}