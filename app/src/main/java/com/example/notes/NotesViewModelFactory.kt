package com.example.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory class responsible for creating instances of [NotesViewModel].
 *
 * @param dao The data access object for interacting with the note database.
 */
class NotesViewModelFactory(private val dao: NoteDao): ViewModelProvider.Factory {
    /**
     * Create an instance of the specified ViewModel class.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @return A new instance of the ViewModel.
     * @throws IllegalArgumentException if the provided class is not assignable to [NotesViewModel].
     */
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(dao) as T
        }
        /**
         * If an unknown ViewModel class is requested, throw an exception
         */
        throw IllegalArgumentException("Unknown ViewModel")
    }
}