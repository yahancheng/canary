package com.chelseatroy.canary.ui.main

import android.app.Dialog
import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.fragment.app.DialogFragment
import com.chelseatroy.canary.R


class MoodEntryFragment : DialogFragment() {
    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.fragment_mood_entry, container, false)
    }


}
