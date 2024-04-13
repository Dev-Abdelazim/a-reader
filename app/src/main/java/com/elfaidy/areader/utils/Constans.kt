package com.elfaidy.areader.utils

import android.icu.text.DateFormat
import com.google.firebase.Timestamp

object Constants {


    const val BASE_URI: String = "https://www.googleapis.com/books/v1/"

    fun formatDate(timestamp: Timestamp): String{
        return DateFormat
            .getDateInstance()
            .format(timestamp.toDate())
            .toString() // march 4, 2023
            .split(",")
            .get(0) // march 4
    }
}