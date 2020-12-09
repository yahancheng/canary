package com.chelseatroy.canary.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chelseatroy.canary.R
import com.chelseatroy.canary.data.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText


/**
 * A placeholder fragment containing a simple view.
 */
class ProfileFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewAdapter: PastimeAdapter

    lateinit var databaseHelper: MoodEntrySQLiteDBHelper
    lateinit var pastimes: ArrayList<PastimeClass>

    lateinit var swipeRefreshLayout: SwipeRefreshLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        return root
    }


    override fun onResume() {
        super.onResume()

        // Because of the !! non-null assertion, the app is going to crash if it can't find this id.
        // I am OK with this because if that happens, the developer will catch that when they run
        // the app to look at the list (so the likelihood that this would go uncaught is very low).

        recyclerView = view?.findViewById(R.id.pastime_list)!!

        databaseHelper = MoodEntrySQLiteDBHelper(activity)
        pastimes = ArrayList<PastimeClass>()

        fetchPastimeData()

        recyclerViewAdapter = PastimeAdapter(activity?.applicationContext!!, pastimes)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext!!)

        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh_profile)!!
        swipeRefreshLayout.setOnRefreshListener {
            fetchPastimeData()
            recyclerViewAdapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }

        val pastimeInput = view?.findViewById<TextInputEditText>(R.id.pastime_input)
        val addPastimeButton = view?.findViewById<Button>(R.id.add_pastime_button)

        if (addPastimeButton != null && pastimeInput != null) {
            addPastimeButton.setOnClickListener {
                val newPastime = pastimeInput.text.toString()
                Log.i("PASTIME INPUT RECEIVED", "Get user input pastime")
                addPastime(newPastime)
                fetchPastimeData()
                view?.findViewById<TextInputEditText>(R.id.pastime_input)?.setText("")
            }
        }

    }

    private fun fetchPastimeData() {
        val cursor = databaseHelper.listPastimeProfile()

        val fromPastimeColumn = cursor.getColumnIndex(MoodEntrySQLiteDBHelper.PASTIME_PROFILE_COLUMN_PASTIME)

        if(cursor.getCount() == 0) {
            Log.i("NO PASTIME", "Fetched data and found none.")
            recyclerViewAdapter.notifyDataSetChanged()
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

    private fun addPastime(
        newPastime: String
    ) {
        val userInputPastime = PastimeClass(newPastime)
        databaseHelper.savePastime(userInputPastime)
        Log.i("SAVE NEW PASTIME TO DB", "Save user input pastime to db")
    }

    companion object {
        @JvmStatic
        fun newInstance(sectionNumber: Int): ProfileFragment {
            return ProfileFragment()
        }
    }
}