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

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(createMoodEntriesTableQuery)
        sqLiteDatabase.execSQL(createPastimeProfileTableQuery)

        save(MoodEntry(Mood.UPSET, 1589842800000, "Rough day", "EXERCISE"), sqLiteDatabase)
        save(MoodEntry(Mood.ELATED, 1589992800800, "HAPPY TO TALK TO MOM", "LINE MOM"), sqLiteDatabase)
        save(MoodEntry(Mood.COPING, 1592002802700, "GOOD MEAL", "HAVE FRIED CHICKEN, EXERCISE"), sqLiteDatabase)
        save(MoodEntry(Mood.NEUTRAL, 1600032806200, "CHILL", "SMOKING"), sqLiteDatabase)
        savePastime(PastimeClass("EXERCISE"), sqLiteDatabase)
        savePastime(PastimeClass("MEDITATION"), sqLiteDatabase)
        savePastime(PastimeClass("HAVE FRIED CHICKEN"), sqLiteDatabase)
        savePastime(PastimeClass("SOCIALIZING"), sqLiteDatabase)
        savePastime(PastimeClass("READING"), sqLiteDatabase)
        savePastime(PastimeClass("SMOKING"), sqLiteDatabase)
        savePastime(PastimeClass("LINE MOM"), sqLiteDatabase)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL(dropMoodEntriesTableQuery)
        sqLiteDatabase.execSQL(dropPastimeProfileTableQuery)
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
    fun savePastime(pastime: PastimeClass) {
        savePastime(pastime, null)
    }

    fun savePastime(
        pastime: PastimeClass,
        database: SQLiteDatabase?
    ) {
        var confirmedDatabase = database
        if(confirmedDatabase == null) {
            confirmedDatabase = MoodEntrySQLiteDBHelper(context).getWritableDatabase()
        }
        val values = ContentValues()

        values.put(PASTIME_PROFILE_COLUMN_PASTIME, pastime.pastime)

        val newRowId = confirmedDatabase?.insert(PASTIME_PROFILE_TABLE_NAME, null, values)

        if (newRowId == -1.toLong() ) {
            Log.wtf("SQLITE INSERTION FAILED", "We don't know why")
        } else {
            Log.i("PASTIME SAVED!", "Saved in row ${newRowId}: ${pastime.pastime}")
        }
    }

    fun deletePastime(pastime: PastimeClass) {
        deletePastime(pastime, null)
    }

    fun deletePastime(
        pastime: PastimeClass,
        database: SQLiteDatabase?
    ) {
        var confirmedDatabase = database
        if(confirmedDatabase == null) {
            confirmedDatabase = MoodEntrySQLiteDBHelper(context).getWritableDatabase()
        }

        val newRowId = confirmedDatabase?.delete(PASTIME_PROFILE_TABLE_NAME, PASTIME_PROFILE_COLUMN_PASTIME+" = ?",
            arrayOf(pastime.pastime))

        if (newRowId == -1 ) {
            Log.wtf("SQLITE DELETE FAILED", "We don't know why")
        } else {
            Log.i("PASTIME DELETED!", "Deleted row ${newRowId}: ${pastime.pastime}")
        }
    }


//    fun newListMoodEntries(): Cursor {
//        val database: SQLiteDatabase = MoodEntrySQLiteDBHelper(context).getReadableDatabase()
//
//        val cursor: Cursor = database.query(
//            MoodEntrySQLiteDBHelper.MOOD_ENTRY_TABLE_NAME,
//            allMoodColumns,
//            null,
//            null,
//            null,
//            null,
//            MoodEntrySQLiteDBHelper.MOOD_ENTRY_COLUMN_LOGGED_AT + " DESC"
//        )
//        Log.i("DATA FETCHED!", "Number of mood entries returned: " + cursor.getCount())
//        return cursor
//    }

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

    fun listPastimeProfile(): Cursor {
        val database: SQLiteDatabase = MoodEntrySQLiteDBHelper(context).getReadableDatabase()

        val cursor: Cursor = database.query(
            MoodEntrySQLiteDBHelper.PASTIME_PROFILE_TABLE_NAME,
            allPastimeColumns,
            null,
            null,
            null,
            null,
            null
        )
        Log.i("DATA FETCHED!", "Number of pastime entries returned: " + cursor.getCount())
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

        // mood entry table
        const val MOOD_ENTRY_TABLE_NAME = "mood_entry"
        const val MOOD_ENTRY_COLUMN_ID = "_id"
        const val MOOD_ENTRY_COLUMN_MOOD = "mood"
        const val MOOD_ENTRY_COLUMN_LOGGED_AT = "logged_at"
        const val MOOD_ENTRY_COLUMN_NOTES = "notes"
        const val MOOD_ENTRY_COLUMN_PASTIMES = "pastimes"

        // pastime profile table
        const val PASTIME_PROFILE_TABLE_NAME = "pastime_profile"
        const val PASTIME_PROFILE_COLUMN_ID = "_id"
        const val PASTIME_PROFILE_COLUMN_PASTIME = "pastime"


        val allMoodColumns = arrayOf<String>(
            MOOD_ENTRY_COLUMN_ID,
            MOOD_ENTRY_COLUMN_MOOD,
            MOOD_ENTRY_COLUMN_LOGGED_AT,
            MOOD_ENTRY_COLUMN_PASTIMES,
            MOOD_ENTRY_COLUMN_NOTES
        )

        val allPastimeColumns = arrayOf<String>(
            PASTIME_PROFILE_COLUMN_ID,
            PASTIME_PROFILE_COLUMN_PASTIME
        )

        val createMoodEntriesTableQuery = "CREATE TABLE ${MOOD_ENTRY_TABLE_NAME} " +
                "(${MOOD_ENTRY_COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${MOOD_ENTRY_COLUMN_MOOD} TEXT, " +
                "${MOOD_ENTRY_COLUMN_LOGGED_AT} INTEGER, " +
                "${MOOD_ENTRY_COLUMN_NOTES} TEXT, " +
                "${MOOD_ENTRY_COLUMN_PASTIMES} TEXT);"

        val createPastimeProfileTableQuery = "CREATE TABLE ${PASTIME_PROFILE_TABLE_NAME} " +
                "(${PASTIME_PROFILE_COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${PASTIME_PROFILE_COLUMN_PASTIME} TEXT);"

        val dropMoodEntriesTableQuery = "DROP TABLE IF EXISTS $MOOD_ENTRY_TABLE_NAME"
        val dropPastimeProfileTableQuery = "DROP TABLE IF EXISTS $PASTIME_PROFILE_TABLE_NAME"


        const val ONE_WEEK_AGO_IN_MILLISECONDS = 604800000
        const val LOGGED_WITHIN = "${MOOD_ENTRY_COLUMN_LOGGED_AT} >= ?"


    }
}
