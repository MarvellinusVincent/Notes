package com.example.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel for managing notes and navigation within the app.
 *
 * @param dao The data access object for interacting with the note database.
 */
class NotesViewModel(val dao: NoteDao) : ViewModel() {

    /**
     * LiveData to hold a list of notes
     */
    val notes = dao.getAll()

    /**
     * LiveData to handle navigation to a specific note
     */
    private val _navigateToNote = MutableLiveData<Long?>()
    val navigateToNote: LiveData<Long?>
        get() = _navigateToNote

    /**
     * Delete a note from the database using a coroutine.
     *
     * @param noteId The ID of the note to be deleted.
     */
    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            val note = Note()
            note.noteId = noteId
            dao.delete(note)
        }
    }

    /**
     * Handle a note item click event by setting the navigation LiveData.
     *
     * @param noteId The ID of the note that was clicked.
     */
    fun onNoteClicked(noteId: Long) {
        _navigateToNote.value = noteId
    }

    /**
     * Reset the navigation LiveData after navigating to a note.
     */
    fun onNoteNavigated() {
        _navigateToNote.value = null
    }
}
