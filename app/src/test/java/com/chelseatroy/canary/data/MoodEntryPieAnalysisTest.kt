package com.chelseatroy.canary.data

import com.github.mikephil.charting.data.PieEntry
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MoodEntryPieAnalysisTest {
    lateinit var systemUnderTest: MoodEntryPieAnalysis

    @Before
    fun setUp() {
        systemUnderTest = MoodEntryPieAnalysis()
    }

    @Test
    fun arrangesPieChartOfMoods() {
        val moodEntries = arrayListOf<MoodEntry>(
            MoodEntry(Mood.ELATED, 1L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 2L, "Note", "EATING"),
            MoodEntry(Mood.COPING, 3L, "Note", "EATING"),
            MoodEntry(Mood.COPING, 4L, "Note", "EATING")
        )
        val pieSections = ArrayList<PieEntry>()
        pieSections.add(PieEntry(0.5F, "ELATED"))
        pieSections.add(PieEntry(0.5F, "COPING"))

        assertEquals(pieSections.toString(), systemUnderTest.getMoodsFrom(moodEntries).toString())
    }

    @Test
    fun arrangesPieChartOfPastime() {
        val moodEntries = arrayListOf<MoodEntry>(
            MoodEntry(Mood.ELATED, 1L, "Note", "EATING, LINE MOM"),
            MoodEntry(Mood.ELATED, 2L, "Note", "EATING, LINE MOM"),
            MoodEntry(Mood.COPING, 3L, "Note", "EATING, HAVE FRIED CHICKEN"),
            MoodEntry(Mood.COPING, 4L, "Note", "EATING, HAVE FRIED CHICKEN")
        )
        val pieSections = ArrayList<PieEntry>()
        pieSections.add(PieEntry(0.5F, "EATING"))
        pieSections.add(PieEntry(0.25F, "LINE MOM"))
        pieSections.add(PieEntry(0.25F, "HAVE FRIED CHICKEN"))

        assertEquals(pieSections.toString(), systemUnderTest.getActivitiesFrom(moodEntries).toString())
    }



}