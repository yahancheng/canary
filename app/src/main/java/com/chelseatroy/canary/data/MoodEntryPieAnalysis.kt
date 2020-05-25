package com.chelseatroy.canary.data

import com.github.mikephil.charting.data.PieEntry

class MoodEntryPieAnalysis() {
    fun getActivitiesFrom(moodEntries: List<MoodEntry>): ArrayList<PieEntry> {
        val pieSections = ArrayList<PieEntry>()
        pieSections.add(PieEntry(20F, "Example A"))
        pieSections.add(PieEntry(50F, "Example B"))
        pieSections.add(PieEntry(30F, "Example C"))
        return pieSections
    }

    fun getMoodsFrom(moodEntries: List<MoodEntry>): ArrayList<PieEntry> {
        val pieSections = ArrayList<PieEntry>()
        pieSections.add(PieEntry(15F, "Example D"))
        pieSections.add(PieEntry(15F, "Example E"))
        pieSections.add(PieEntry(70F, "Example F"))
        return pieSections
    }
}