package com.chelseatroy.canary.data

import java.time.LocalDateTime

class MoodEntry(mood: Mood) {
    val notes: String = ""
    val loggedAt: LocalDateTime = LocalDateTime.now()
    val recentActivities = arrayListOf<Pastime>()
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