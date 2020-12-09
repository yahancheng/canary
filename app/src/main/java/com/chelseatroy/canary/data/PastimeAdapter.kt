package com.chelseatroy.canary.data

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chelseatroy.canary.R
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.pastime_list
import kotlinx.android.synthetic.main.item_list_pastime.view.*

class PastimeAdapter(context: Context, pastime: ArrayList<PastimeClass>) :
    RecyclerView.Adapter<PastimeAdapter.PastimeViewHolder>() {
    var context: Context = context
    var pastime: ArrayList<PastimeClass> = pastime
    lateinit var databaseHelper: MoodEntrySQLiteDBHelper

    inner class PastimeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var pastimeTextView: TextView = itemView.findViewById(R.id.available_pastime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastimeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val pastimeListItem: View = inflater
            .inflate(R.layout.item_list_pastime, parent, false)
        databaseHelper = MoodEntrySQLiteDBHelper(context)
        return PastimeViewHolder(pastimeListItem)
    }

    override fun getItemCount(): Int {
        return pastime.size
    }

    override fun onBindViewHolder(holder: PastimeViewHolder, position: Int) {
        holder.pastimeTextView.text = PastimeClass.formatForView(pastime[position].pastime)
        val deletePastimeButton = holder.itemView.findViewById<ImageButton>(R.id.delete_pastime_Button)
        deletePastimeButton?.setOnClickListener {
            Log.i("PASTIME INPUT RECEIVED", "Deleting user input pastime")
            startDeleteDialog(position, holder.itemView.context)
        }
    }

    private fun deletePastime(
        pastime: PastimeClass
    ) {
        databaseHelper.deletePastime(pastime)
        Log.i("DELETE PASTIME", "Remove pastime")
    }

    private fun startDeleteDialog(position: Int, context: Context)
    {
        AlertDialog.Builder(context)
            .setTitle("Are you sure you want to delete this activity?")
            .setPositiveButton("OK") { _, _ ->
                if (position > -1) {
                    val delPastime = pastime[position]
                    deletePastime(delPastime)
                    pastime.remove(delPastime)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)
                }
            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}


