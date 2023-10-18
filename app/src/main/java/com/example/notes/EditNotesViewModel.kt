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
class EditNotesViewModel(noteId: Long, val dao: NoteDao) : ViewModel() {
    /**
     * LiveData representing the note being edited.
     */
    var note = MutableLiveData<Note>()
    val noteId : Long = noteId

    /**
     * Initializes the ViewModel and logs the noteId being used.
     */
    init {
        Log.d("EditNotesViewModel", "noteId used: $noteId")
        dao.get(noteId).observeForever{ it ->
            if(it == null) {
                note.value = Note()
            } else {
                note.value = it
            }
        }
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
            if(note.value?.noteId != 0L) {
                dao.update(note.value!!)
            } else {
                dao.insert(note.value!!)
            }
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

