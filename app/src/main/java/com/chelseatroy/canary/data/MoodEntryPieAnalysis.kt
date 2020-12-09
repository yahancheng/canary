package com.chelseatroy.canary.data

import android.util.Log
import com.github.mikephil.charting.data.PieEntry

class MoodEntryPieAnalysis() {
    fun getActivitiesFrom(moodEntries: List<MoodEntry>): ArrayList<PieEntry> {
        val pieSections = ArrayList<PieEntry>()
        val countPastimeTable = mutableMapOf<String, Int?>()
        countPastimeTable["Total"] = 0
        for (moodEntry in moodEntries) {
            for (pastime in moodEntry.recentPastimes) {
                if (pastime.pastime != "") {
                    if (countPastimeTable.containsKey(pastime.pastime)) {
                        countPastimeTable[pastime.pastime] = countPastimeTable[pastime.pastime]?.plus(1)
                        countPastimeTable["Total"] = countPastimeTable["Total"]?.plus(1)
                    } else {
                        countPastimeTable[pastime.pastime] = 1
                        countPastimeTable["Total"] = countPastimeTable["Total"]?.plus(1)
                    }
                }
            }
        }
        Log.i("countPastimeTable", print(countPastimeTable).toString())

        for (pastimeKey in countPastimeTable.keys) {
            if (pastimeKey != "Total") {
                var proportion: Float = divide(countPastimeTable[pastimeKey]!!, countPastimeTable["Total"]!!)
                pieSections.add(PieEntry(proportion, pastimeKey))
            }
        }
        return pieSections
    }

    fun getMoodsFrom(moodEntries: List<MoodEntry>): ArrayList<PieEntry> {
        val pieSections = ArrayList<PieEntry>()
        val countMoodTable = mutableMapOf<String, Int?>()
        countMoodTable["Total"] = 0
        for (moodEntry in moodEntries) {
            var moodString = moodEntry.mood.toString()
            if (countMoodTable.containsKey(moodString)) {
                countMoodTable[moodString] = countMoodTable[moodString]?.plus(1)
                countMoodTable["Total"] = countMoodTable["Total"]?.plus(1)
            } else {
                countMoodTable[moodString] = 1
                countMoodTable["Total"] = countMoodTable["Total"]?.plus(1)
            }
        }
        Log.i("countMoodTable", print(countMoodTable).toString())


        for (moodKey in countMoodTable.keys) {
            if (moodKey != "Total") {
                var proportion: Float = divide(countMoodTable[moodKey]!!, countMoodTable["Total"]!!)
                pieSections.add(PieEntry(proportion, moodKey))
            }
        }
        return pieSections
    }

    fun divide(a:Int, b:Int): Float {
        return a.toFloat() / b.toFloat()
    }
}