package com.example.myapplication1.database

object DbContract {

    const val DATABASE_NAME = "bus_booking.db"
    const val DATABASE_VERSION = 1

    object Users {
        const val TABLE_NAME = "users"
        const val ID = "id"
        const val FULL_NAME = "full_name"
        const val UNIVERSITY_ID = "university_id"
        const val PASSWORD = "password"
        const val USER_TYPE = "user_type"
    }

    object Notifications {
        const val TABLE_NAME = "notifications"
        const val ID = "id"
        const val TITLE = "title"
        const val MESSAGE = "message"
        const val TIME = "time"
        const val IS_READ = "is_read"
    }

    object UserType {
        const val STUDENT = "student"
        const val DRIVER = "driver"
    }
}