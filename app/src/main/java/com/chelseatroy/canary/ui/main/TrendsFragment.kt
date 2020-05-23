package com.chelseatroy.canary.ui.main

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chelseatroy.canary.R
import com.chelseatroy.canary.data.Mood
import com.chelseatroy.canary.data.MoodEntry
import com.chelseatroy.canary.data.MoodEntrySQLiteDBHelper
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener


class TrendsFragment : Fragment() {

    lateinit var scatterPlot: ScatterChart
    lateinit var moodEntries: List<MoodEntry>
    lateinit var databaseHelper: MoodEntrySQLiteDBHelper
    lateinit var markerView: ScatterplotMarkerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_trends, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()
        scatterPlot = view?.findViewById(R.id.line_chart)!!

        databaseHelper = MoodEntrySQLiteDBHelper(activity)
        moodEntries = databaseHelper.fetchMoodData()

        if (moodEntries.isNotEmpty()) {
            moodEntries = moodEntries.reversed().toList()
            arrangeScatterPlot()
        }
    }

    private fun arrangeScatterPlot() {
        var xPositions = getXPositionsFor(moodEntries)

        val dataSet = ArrayList<Entry>()

        for ((index, entry) in moodEntries.withIndex()) {
            dataSet.add(
                Entry(
                    xPositions.get(index),
                    getYPositionFor(entry),
                    getMoodIcon(entry),
                    MoodEntry.getFormattedLogTime(entry.loggedAt)))
        }

        val scatterDataSet = ScatterDataSet(dataSet, "")
        scatterDataSet.setDrawValues(false)

        scatterPlot.data = ScatterData(scatterDataSet)

        scatterPlot.axisRight.isEnabled = false
        scatterPlot.axisLeft.isEnabled = false
        scatterPlot.xAxis.isEnabled = false
        scatterPlot.description.text = ""
        scatterPlot.legend.isEnabled = false
        scatterPlot.setBackgroundColor(activity?.resources!!.getColor(R.color.canaryBackgroundColor)!!)

        scatterPlot.setTouchEnabled(true)
        scatterPlot.setPinchZoom(true)

        scatterPlot.setNoDataText("No moods entered yet!")

        markerView = ScatterplotMarkerView(activity, R.layout.view_marker_scatterplot)
        scatterPlot.setMarkerView(markerView)


        scatterPlot.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(
                e: Entry,
                h: Highlight?
            ) {
                scatterPlot.highlightValue(h)
                markerView.moodEntryLabel.text = e.data.toString()
            }

            override fun onNothingSelected() {}
        })

    }

    private fun getMoodIcon(moodEntry: MoodEntry): BitmapDrawable {
        var face = getResources().getDrawable(R.drawable.neutral)
        when (moodEntry.mood) {
            Mood.UPSET -> face = getResources().getDrawable(R.drawable.upset);
            Mood.DOWN -> face = getResources().getDrawable(R.drawable.down);
            Mood.NEUTRAL -> face = getResources().getDrawable(R.drawable.neutral);
            Mood.COPING -> face = getResources().getDrawable(R.drawable.coping);
            Mood.ELATED -> face = getResources().getDrawable(R.drawable.elated);
        }
        val bitmap = (face as BitmapDrawable).bitmap
        val imageresource =
            BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 30, 30, true))
        return imageresource
    }

    private fun getYPositionFor(moodEntry: MoodEntry): Float {
        var height = 0f
        when (moodEntry.mood) {
            Mood.UPSET -> height = 1f
            Mood.DOWN -> height = 2f
            Mood.NEUTRAL -> height = 3f
            Mood.COPING -> height = 4f
            Mood.ELATED -> height = 5f
        }
        return height
    }

    private fun getXPositionsFor(moodEntries: List<MoodEntry>): List<Float> {
        val latestPoint = moodEntries.last().loggedAt
        val earliestPoint = moodEntries.first().loggedAt
        val diff = latestPoint - earliestPoint

        if (diff == 0L) {
            return arrayListOf(0f) // If we don't do this, a singleton list with 0L in it becomes [NaN] for some reason
        } else {
            return moodEntries.map { (it.loggedAt.toFloat() - earliestPoint) / diff } as ArrayList<Float>
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(sectionNumber: Int): TrendsFragment {
            return TrendsFragment()
        }
    }
}