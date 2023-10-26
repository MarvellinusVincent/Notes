package com.example.notes

import com.google.firebase.database.Exclude

/**
 * Data class representing a Note.
 *
 * @property noteId Unique identifier for the note, excluded from Firebase database.
 * @property noteName The name or title of the note.
 * @property noteDescription The description or content of the note.
 */
data class Note(
    @get:Exclude
    var noteId: String = "",
    var noteName: String = "",
    var noteDescription: String = ""
)
