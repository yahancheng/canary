package com.chelseatroy.canary.data

import org.junit.Assert.assertEquals
import org.junit.Test

class MoodEntrySQLiteDBHelperTest {
    @Test
    fun creationQuery_assemblesMoodEntryTable() {
        val expectedQuery = "CREATE TABLE mood_entry (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "mood TEXT, " +
                "logged_at INTEGER, " +
                "notes TEXT, " +
                "pastimes TEXT);"
        assertEquals(expectedQuery, MoodEntrySQLiteDBHelper.createMoodEntriesTableQuery)
    }

    @Test
    fun deletionQuery_removesMoodEntryTable() {
        val expectedQuery = "DROP TABLE IF EXISTS mood_entry"
        assertEquals(expectedQuery, MoodEntrySQLiteDBHelper.dropMoodEntriesTableQuery)
    }

}