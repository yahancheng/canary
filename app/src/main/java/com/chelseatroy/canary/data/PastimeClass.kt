package com.chelseatroy.canary.data

import android.util.Log

class PastimeClass(name: String) {
    val pastime = name

    companion object {
        fun formatForView(pastime: String): String {
            val stringifiedPastimes = pastime
            return if (stringifiedPastimes.isEmpty()) "Invalid pastime :(" else stringifiedPastimes
        }
    }
}