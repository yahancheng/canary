package com.chelseatroy.canary.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MoodEntrySQLiteDBHelper(context: Context?) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    val context = context

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(createMoodEntriesTableQuery)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL(dropMoodEntriesTableQuery)
        onCreate(sqLiteDatabase)
    }

    fun save(moodEntry: MoodEntry) {
        val database: SQLiteDatabase = MoodEntrySQLiteDBHelper(context).getWritableDatabase()
        val values = ContentValues()

        values.put(MOOD_ENTRY_COLUMN_MOOD, moodEntry.mood.toString())
        values.put(MOOD_ENTRY_COLUMN_LOGGED_AT, moodEntry.loggedAt)
        values.put(MOOD_ENTRY_COLUMN_NOTES, moodEntry.notes)
        values.put(MOOD_ENTRY_COLUMN_PASTIMES, MoodEntry.formatForDatabase(moodEntry.recentPastimes))

        val newRowId = database.insert(MOOD_ENTRY_TABLE_NAME, null, values)

        if (newRowId == -1.toLong() ) {
            Log.wtf("SQLITE INSERTION FAILED", "We don't know why")
        } else {
            Log.i("MOOD ENTRY SAVED!", "Saved in row ${newRowId}: ${moodEntry.toString()}")
        }
    }

    fun listMoodEntries(): Cursor {
        val database: SQLiteDatabase = MoodEntrySQLiteDBHelper(context).getReadableDatabase()

        val cursor: Cursor = database.query(
            MoodEntrySQLiteDBHelper.MOOD_ENTRY_TABLE_NAME,
            allMoodColumns,
            null,
            null,
            null,
            null,
            MoodEntrySQLiteDBHelper.MOOD_ENTRY_COLUMN_LOGGED_AT + " DESC"
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

    }
}
