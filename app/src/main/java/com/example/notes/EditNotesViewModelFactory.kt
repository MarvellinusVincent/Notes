package com.example.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory class for creating EditNotesViewModel instances.
 *
 * @param nodeId The ID of the note to edit.
 * @param dao The data access object for interacting with notes.
 */
class EditNotesViewModelFactory(private val nodeId: Long, private val dao: NoteDao) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of the specified ViewModel.
     *
     * @param modelClass The class of the ViewModel to create.
     * @throws IllegalArgumentException if the ViewModel class is unknown.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditNotesViewModel::class.java)) {
            return EditNotesViewModel(nodeId, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
