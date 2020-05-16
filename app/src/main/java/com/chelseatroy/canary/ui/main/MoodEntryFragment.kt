package com.chelseatroy.canary.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.chelseatroy.canary.R
import com.chelseatroy.canary.data.Mood
import com.chelseatroy.canary.data.Pastime
import com.google.android.material.snackbar.Snackbar
import kotlin.math.log


class MoodEntryFragment : DialogFragment() {
    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */
    lateinit var currentMood: Mood

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.fragment_mood_entry, container, false)
    }

    override fun onResume() {
        super.onResume()

        val checkboxList = view?.findViewById<LinearLayout>(R.id.activities_checkbox_list)
        Pastime.values().forEach {
            val checkity = CheckBox(activity)
            checkity.setText(it.name)
            checkboxList?.addView(checkity)
        }

        val upset = view?.findViewById<ImageView>(R.id.ic_upset)
        val down = view?.findViewById<ImageView>(R.id.ic_down)
        val neutral = view?.findViewById<ImageView>(R.id.ic_neutral)
        val coping = view?.findViewById<ImageView>(R.id.ic_coping)
        val elated = view?.findViewById<ImageView>(R.id.ic_elated)
        val moodButtonCollection = listOf(upset, down, neutral, coping, elated)

        for ((index, button) in moodButtonCollection.withIndex()) {
            button?.setOnClickListener({ view ->
                for (button in moodButtonCollection) { button?.setBackgroundColor(resources.getColor(R.color.design_default_color_background)) }
                view.setBackgroundColor(resources.getColor(R.color.colorAccent));
                currentMood = Mood.values()[index]
            })
        }

        //make the mood images clickable and exclusive

        val logMoodButton = view?.findViewById<Button>(R.id.log_mood_button)
        logMoodButton?.setOnClickListener({ view ->
            if (!this::currentMood.isInitialized) {
                val formValidationNotification = Snackbar.make(
                    view,
                    "Please select a mood icon to record your mood!",
                    Snackbar.LENGTH_LONG
                )
                formValidationNotification.show()
            }
        })
    }

}
