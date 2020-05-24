package com.chelseatroy.canary.data

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MoodEntryScatterAnalysisTest {
    lateinit var systemUnderTest: MoodEntryScatterAnalysis

    @Before
    fun setUp() {
        systemUnderTest = MoodEntryScatterAnalysis()
    }

    @Test
    fun arragesMoodsVertically_basedOnMoodHappiness() {
        assertEquals(1f, systemUnderTest.getYPositionFor(MoodEntry(Mood.UPSET)))
        assertEquals(2f, systemUnderTest.getYPositionFor(MoodEntry(Mood.DOWN)))
        assertEquals(3f, systemUnderTest.getYPositionFor(MoodEntry(Mood.NEUTRAL)))
        assertEquals(4f, systemUnderTest.getYPositionFor(MoodEntry(Mood.COPING)))
        assertEquals(5f, systemUnderTest.getYPositionFor(MoodEntry(Mood.ELATED)))
    }

    @Test
    fun arrangesMoodsHorizontally_proportionalToWhenTheyWereLogged() {
        val regularlySpacedMoodEntries = arrayListOf<MoodEntry>(
            MoodEntry(Mood.ELATED, 1L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 2L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 3L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 4L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 5L, "Note", "EATING")
        )

        assertEquals(arrayListOf(0.0f, 0.25f, 0.5f, 0.75f, 1.0f), systemUnderTest.getXPositionsFor(regularlySpacedMoodEntries))
    }
}