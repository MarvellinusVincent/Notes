package com.example.notes

import com.google.firebase.database.Exclude

data class Note(
    @get:Exclude
    var noteId: String = "",
    var noteName: String = "",
    var noteDescription: String = ""
)