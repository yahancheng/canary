package com.chelseatroy.canary.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chelseatroy.canary.R
import com.chelseatroy.canary.data.Mood
import com.chelseatroy.canary.data.MoodEntry
import com.chelseatroy.canary.data.MoodEntryPieAnalysis
import com.chelseatroy.canary.data.MoodEntrySQLiteDBHelper
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_tags.*

class TagsFragment : Fragment() {
    lateinit var moodEntries: List<MoodEntry>
    lateinit var databaseHelper: MoodEntrySQLiteDBHelper

    lateinit var pieAnalysis: MoodEntryPieAnalysis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tags, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()

        databaseHelper = MoodEntrySQLiteDBHelper(activity)

        moodEntries = ArrayList()
        (moodEntries as ArrayList<MoodEntry>).clear()
        (moodEntries as ArrayList<MoodEntry>).addAll(
            databaseHelper.fetchMoodData()
        )

        pieAnalysis = MoodEntryPieAnalysis()


        if (moodEntries.isNotEmpty()) {
            moodEntries = moodEntries.reversed().toList()
            assemblePieChart(
                moodsPieChart,
                getString(R.string.moods),
                pieAnalysis.getMoodsFrom(moodEntries),
                listColors()
            )
            assemblePieChart(
                pieChart = activitiesPieChart,
                name = getString(R.string.pastimes),
                pieSections = pieAnalysis.getActivitiesFrom(moodEntries),
                listColors = listColors()
            )
        }
    }

    fun listColors(): ArrayList<Int> {
        val listColors = ArrayList<Int>()
        listColors.add(resources.getColor(R.color.colorAccent))
        listColors.add(resources.getColor(R.color.colorPie1))
        listColors.add(resources.getColor(R.color.colorPie2))
        listColors.add(resources.getColor(R.color.colorPie3))
        listColors.add(resources.getColor(R.color.colorPie4))
        listColors.add(resources.getColor(R.color.colorPie5))
        listColors.add(resources.getColor(R.color.colorPie6))
        listColors.add(resources.getColor(R.color.colorPie7))
        return listColors
    }

    fun assemblePieChart(
        pieChart: PieChart,
        name: String,
        pieSections: ArrayList<PieEntry>,
        listColors: ArrayList<Int>
    ) {
        moodsPieChart.setUsePercentValues(true)

        pieChart.description.text = name
        pieChart.description.textSize = 20f
        pieChart.isDrawHoleEnabled = true

        val pieDataSet = PieDataSet(pieSections, "")
        pieDataSet.colors = listColors

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(20f)
        pieChart.data = pieData

        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelColor(R.color.colorPrimaryDark)
        pieChart.legend.isEnabled = false

        pieChart.animateY(3000, Easing.EaseInOutQuad)
    }

    companion object {
        @JvmStatic
        fun newInstance(): TagsFragment {
            return TagsFragment()
        }
    }
}