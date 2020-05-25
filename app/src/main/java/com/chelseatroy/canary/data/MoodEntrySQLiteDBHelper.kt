package com.chelseatroy.canary.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class MoodEntrySQLiteDBHelper(context: Context?) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    val context = context

    //region Plumbing
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(createMoodEntriesTableQuery)

        save(MoodEntry(Mood.UPSET, 1589842800000, "Rough day", "EXERCISE, SLEEP"), sqLiteDatabase)
        save(MoodEntry(Mood.DOWN, 1589896800000, "Still not so happy", "EXERCISE"), sqLiteDatabase)
        save(MoodEntry(Mood.NEUTRAL, 1589911200000, "A little better", "SLEEP"), sqLiteDatabase)
        save(MoodEntry(Mood.UPSET, 1589997600000, "Another rough day", "EXERCISE"), sqLiteDatabase)
        save(MoodEntry(Mood.COPING, 1590084000000, "Much better", "SOCIALIZING"), sqLiteDatabase)
        save(MoodEntry(Mood.NEUTRAL, 1590170400000, "Lots of work", "SLEEP"), sqLiteDatabase)
        save(MoodEntry(Mood.COPING, 1590256800000, "Got some sunshine", "EXERCISE"), sqLiteDatabase)
        save(MoodEntry(Mood.ELATED, 1590271200000, "Got some praise!", "SOCIALIZING"), sqLiteDatabase)
        save(MoodEntry(Mood.NEUTRAL, 1590357600000, "Wish I could hug my friends", "SLEEP"), sqLiteDatabase)
        save(MoodEntry(Mood.NEUTRAL, 1590422400000, "Work again", "EXERCISE"), sqLiteDatabase)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL(dropMoodEntriesTableQuery)
        onCreate(sqLiteDatabase)
    }

    fun save(moodEntry: MoodEntry) {
        save(moodEntry, null)
    }

    fun save(
        moodEntry: MoodEntry,
        database: SQLiteDatabase?
    ) {
        var confirmedDatabase = database
        if(confirmedDatabase == null) {
            confirmedDatabase = MoodEntrySQLiteDBHelper(context).getWritableDatabase()
        }
        val values = ContentValues()

        values.put(MOOD_ENTRY_COLUMN_MOOD, moodEntry.mood.toString())
        values.put(MOOD_ENTRY_COLUMN_LOGGED_AT, moodEntry.loggedAt)
        values.put(MOOD_ENTRY_COLUMN_NOTES, moodEntry.notes)
        values.put(
            MOOD_ENTRY_COLUMN_PASTIMES,
            MoodEntry.formatForDatabase(moodEntry.recentPastimes)
        )

        val newRowId = confirmedDatabase?.insert(MOOD_ENTRY_TABLE_NAME, null, values)

        if (newRowId == -1.toLong()) {
            Log.wtf("SQLITE INSERTION FAILED", "We don't know why")
        } else {
            Log.i("MOOD ENTRY SAVED!", "Saved in row ${newRowId}: ${moodEntry.toString()}")
        }
    }

    fun listMoodEntries(limitToPastWeek: Boolean = false): Cursor {
        val database: SQLiteDatabase = MoodEntrySQLiteDBHelper(context).getReadableDatabase()

        var filterOnThis: String? = null
        var usingTheseValues: Array<String>? = null

        if (limitToPastWeek) {
            val nowInMilliseconds = Calendar.getInstance().timeInMillis.toInt()
            filterOnThis = LOGGED_WITHIN
            usingTheseValues = arrayOf("${nowInMilliseconds - ONE_WEEK_AGO_IN_MILLISECONDS}")
        }

        val cursor: Cursor = database.query(
            MOOD_ENTRY_TABLE_NAME,
            allMoodColumns,
            filterOnThis,
            usingTheseValues,
            null,
            null,
            MOOD_ENTRY_COLUMN_LOGGED_AT + " DESC"
        )
        Log.i("DATA FETCHED!", "Number of mood entries returned: " + cursor.getCount())
        return cursor
    }

    fun create() {
        onCreate(writableDatabase)
    }

    fun clear() {
        writableDatabase.execSQL("DROP TABLE IF EXISTS $MOOD_ENTRY_TABLE_NAME")
    }
    //endregion

    //region Porcelain

    fun fetchMoodData(limitToPastWeek: Boolean = false): ArrayList<MoodEntry> {
        var moodEntries = ArrayList<MoodEntry>()
        val cursor = listMoodEntries(limitToPastWeek = limitToPastWeek)

        val fromMoodColumn = cursor.getColumnIndex(MoodEntrySQLiteDBHelper.MOOD_ENTRY_COLUMN_MOOD)
        val fromNotesColumn = cursor.getColumnIndex(MoodEntrySQLiteDBHelper.MOOD_ENTRY_COLUMN_NOTES)
        val fromLoggedAtColumn =
            cursor.getColumnIndex(MOOD_ENTRY_COLUMN_LOGGED_AT)
        val fromPastimesColumn =
            cursor.getColumnIndex(MoodEntrySQLiteDBHelper.MOOD_ENTRY_COLUMN_PASTIMES)

        if (cursor.getCount() == 0) {
            Log.i("NO MOOD ENTRIES", "Fetched data and found none.")
        } else {
            Log.i("MOOD ENTRIES FETCHED!", "Fetched data and found mood entries.")
            while (cursor.moveToNext()) {
                val nextMood = MoodEntry(
                    Mood.valueOf(cursor.getString(fromMoodColumn)),
                    cursor.getLong(fromLoggedAtColumn),
                    cursor.getString(fromNotesColumn),
                    cursor.getString(fromPastimesColumn)
                )
                moodEntries.add(nextMood)
            }
        }
        return moodEntries
    }

    //endregion

    companion object {
        private const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "canary_database"
        const val MOOD_ENTRY_TABLE_NAME = "mood_entry"
        const val MOOD_ENTRY_COLUMN_ID = "_id"
        const val MOOD_ENTRY_COLUMN_MOOD = "mood"
        const val MOOD_ENTRY_COLUMN_LOGGED_AT = "logged_at"
        const val MOOD_ENTRY_COLUMN_NOTES = "notes"
        const val MOOD_ENTRY_COLUMN_PASTIMES = "pastimes"

        val allMoodColumns = arrayOf<String>(
            MOOD_ENTRY_COLUMN_ID,
            MOOD_ENTRY_COLUMN_MOOD,
            MOOD_ENTRY_COLUMN_LOGGED_AT,
            MOOD_ENTRY_COLUMN_PASTIMES,
            MOOD_ENTRY_COLUMN_NOTES
        )

        val createMoodEntriesTableQuery = "CREATE TABLE ${MOOD_ENTRY_TABLE_NAME} " +
                "(${MOOD_ENTRY_COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${MOOD_ENTRY_COLUMN_MOOD} TEXT, " +
                "${MOOD_ENTRY_COLUMN_LOGGED_AT} INTEGER, " +
                "${MOOD_ENTRY_COLUMN_NOTES} TEXT, " +
                "${MOOD_ENTRY_COLUMN_PASTIMES} TEXT);"

        val dropMoodEntriesTableQuery = "DROP TABLE IF EXISTS $MOOD_ENTRY_TABLE_NAME"

        const val ONE_WEEK_AGO_IN_MILLISECONDS = 604800000
        const val LOGGED_WITHIN = "${MOOD_ENTRY_COLUMN_LOGGED_AT} >= ?"

    }
}
