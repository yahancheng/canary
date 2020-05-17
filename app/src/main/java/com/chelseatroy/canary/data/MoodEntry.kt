package com.chelseatroy.canary.data

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MoodEntry(mood: Mood) {
    val mood = mood
    var notes: String = ""
    var loggedAt: String? = getCurrentTime()
    var recentPastimes = arrayListOf<Pastime>()


    fun getCurrentTime(): String? {
        val DATE_FORMAT = "dd-MMM-yyyy hh:mm a"
        val dateFormat = SimpleDateFormat(DATE_FORMAT)
        dateFormat.setTimeZone(TimeZone.getDefault())
        val today: Date = Calendar.getInstance().getTime()
        return dateFormat.format(today)
    }

    override fun toString(): String {
        return "Mood Entry(" +
                "mood: ${this.mood}, " +
                "notes: ${this.notes}, " +
                "pastimes: ${this.recentPastimes}"
    }
}

enum class Mood {
    UPSET, DOWN, NEUTRAL, COPING, ELATED
}

enum class Pastime {
    EXERCISE,
    MEDITATION,
    SOCIALIZING,
    HYDRATING,
    EATING,
    BIRDWATCHING,
    READING,
    TELEVISION,
    SLEEP,
    STRETCHING,
    CARING_FOR_OTHERS,
    JOURNALING
}