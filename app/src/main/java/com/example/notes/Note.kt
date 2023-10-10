package com.example.notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a note in the database.
 *
 * @param noteId The unique ID of the note (auto-generated).
 * @param noteName The name or title of the note.
 * @param noteDescription The description or content of the note.
 */
@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var noteId: Long = 0L,

    @ColumnInfo(name = "note_name")
    var noteName: String = "",

    @ColumnInfo(name = "note_description")
    var noteDescription: String = ""
)

