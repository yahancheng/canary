package com.chelseatroy.canary.data

import org.junit.Assert.*
import org.junit.Test

class MoodEntryTest {
    @Test
    fun toString_stringifiesMoodEntry() {
        val moodEntry = MoodEntry(Mood.ELATED)
        moodEntry.recentPastimes = arrayListOf(Pastime.EATING, Pastime.READING, Pastime.SLEEP)
        moodEntry.notes = "Had a decent day!"

        assertEquals("Mood Entry(mood: ELATED, notes: Had a decent day!, pastimes: [EATING, READING, SLEEP]",
        moodEntry.toString())
    }
}