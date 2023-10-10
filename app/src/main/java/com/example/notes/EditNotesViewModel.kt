package com.example.notes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel for editing notes.
 *
 * @param noteId The ID of the note to edit.
 * @param dao The data access object for interacting with notes.
 */
class EditNotesViewModel(val noteId: Long, val dao: NoteDao) : ViewModel() {
    /**
     * LiveData representing the note being edited.
     */
    val note = dao.get(noteId)

    /**
     * Initializes the ViewModel and logs the noteId being used.
     */
    init {
        Log.d("EditNotesViewModel", "noteId used: $noteId")
    }

    /**
     * MutableLiveData for navigation to the notes list.
     */
    private val _navigateToList = MutableLiveData<Boolean>(false)

    /**
     * LiveData for observing navigation to the notes list.
     */
    val navigateToList: LiveData<Boolean>
        get() = _navigateToList

    /**
     * Saves the edited note to the database.
     */
    fun saveNote() {
        viewModelScope.launch {
            Log.d("EditNotesViewModel", "saveNote called with noteId: $noteId")
            dao.update(note.value!!)
            _navigateToList.value = true
        }
    }

    /**
     * Resets the navigation flag after navigating to the notes list.
     */
    fun onNavigatedToList() {
        _navigateToList.value = false
    }
}

