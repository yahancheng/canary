package com.chelseatroy.canary.ui.main


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.chelseatroy.canary.R

import com.chelseatroy.canary.data.*
import com.chelseatroy.canary.Updatable
import com.chelseatroy.canary.data.Mood
import com.chelseatroy.canary.data.MoodEntry
import com.chelseatroy.canary.data.MoodEntrySQLiteDBHelper
import com.chelseatroy.canary.data.PastimeClass
import com.google.android.material.snackbar.Snackbar


class MoodEntryFragment : DialogFragment() {
    lateinit var currentMood: Mood

    lateinit var databaseHelper: MoodEntrySQLiteDBHelper
    lateinit var pastimes: ArrayList<PastimeClass>
    lateinit var dismissListener: Updatable


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_mood_entry, container, false)
    }

    override fun onResume() {
        super.onResume()

        assembleMoodButtons()
        val checkBoxList = assemblePastimeCheckBoxes()

        val notesEditText = view?.findViewById<EditText>(R.id.mood_notes)
        val logMoodButton = view?.findViewById<Button>(R.id.log_mood_button)

        logMoodButton?.setOnClickListener({ view ->
            if (!this::currentMood.isInitialized) {
                instructGuestToChooseAMood(view)
            } else {
                submitMoodEntry(checkBoxList, notesEditText)
                dismiss()
            }
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (dismissListener != null) {
            dismissListener.onDismissal()
        }
    }

    private fun submitMoodEntry(
        checkBoxList: ArrayList<CheckBox>,
        notesEditText: EditText?
    ) {
        var moodEntry = MoodEntry(currentMood)
        moodEntry.recentPastimes = ArrayList(
            checkBoxList
                .filter { checkBox -> checkBox.isChecked }
                .map { checkBox -> PastimeClass(checkBox.text.toString()) })

        moodEntry.notes = notesEditText?.text.toString()

        Log.i("SUBMITTED MOOD ENTRY", moodEntry.toString())

        val databaseHelper = MoodEntrySQLiteDBHelper(activity)
        databaseHelper.save(moodEntry)
    }

    private fun instructGuestToChooseAMood(view: View) {
        val formValidationMessage = Snackbar.make(
            view,
            "Please select a mood icon to record your mood!",
            Snackbar.LENGTH_LONG
        )
        formValidationMessage.show()
    }

    private fun informGuestOfMoodEntryCreation(view: View) {
        val confirmationMessage = Snackbar.make(
            view,
            "We've logged your mood!",
            Snackbar.LENGTH_LONG
        )
        confirmationMessage.show()
    }

    private fun assemblePastimeCheckBoxes(): ArrayList<CheckBox> {
        val checkBoxLayout = view?.findViewById<LinearLayout>(R.id.pastimes_checkbox_list)
        val checkBoxList = arrayListOf<CheckBox>()

        databaseHelper = MoodEntrySQLiteDBHelper(activity)
        pastimes = ArrayList<PastimeClass>()

        fetchPastimeData()

        pastimes.forEach {
            val pastimeCheckBox = CheckBox(activity)
            pastimeCheckBox.setText(it.pastime)
            checkBoxLayout?.addView(pastimeCheckBox)
            checkBoxList.add(pastimeCheckBox)
        }
        return checkBoxList
    }

    private fun assembleMoodButtons() {
        val upset = view?.findViewById<ImageView>(R.id.ic_upset)
        val down = view?.findViewById<ImageView>(R.id.ic_down)
        val neutral = view?.findViewById<ImageView>(R.id.ic_neutral)
        val coping = view?.findViewById<ImageView>(R.id.ic_coping)
        val elated = view?.findViewById<ImageView>(R.id.ic_elated)
        val moodButtonCollection = listOf(upset, down, neutral, coping, elated)

        val unselectedColor = resources.getColor(R.color.design_default_color_background)
        val selectedColor = resources.getColor(R.color.colorAccent)

        for ((index, button) in moodButtonCollection.withIndex()) {
            button?.setOnClickListener({ view ->
                for (button in moodButtonCollection) button?.setBackgroundColor(unselectedColor)
                view.setBackgroundColor(selectedColor)
                currentMood = Mood.values()[index]
            })
        }
    }

    fun fetchPastimeData() {
        val cursor = databaseHelper.listPastimeProfile()

        val fromPastimeColumn = cursor.getColumnIndex(MoodEntrySQLiteDBHelper.PASTIME_PROFILE_COLUMN_PASTIME)

        if(cursor.getCount() == 0) {
            Log.i("NO PASTIME", "Fetched data and found none.")
        } else {
            Log.i("PASTIMES FETCHED!", "Fetched data and found pastimes.")
            pastimes.clear()

            while (cursor.moveToNext()) {
                val nextPastime = PastimeClass(
                    cursor.getString(fromPastimeColumn)
                )
                pastimes.add(nextPastime)
            }
        }
    }
}
