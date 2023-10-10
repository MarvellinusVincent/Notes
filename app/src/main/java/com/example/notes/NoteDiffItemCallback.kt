package com.example.notes

import androidx.recyclerview.widget.DiffUtil

/**
 * Callback class for calculating the difference between two lists of notes.
 */
class NoteDiffItemCallback : DiffUtil.ItemCallback<Note>() {
    /**
     * Called to check whether two items represent the same object in the list.
     *
     * @param oldItem The old item.
     * @param newItem The new item.
     * @return `true` if the items have the same ID, `false` otherwise.
     */
    override fun areItemsTheSame(oldItem: Note, newItem: Note)
            = (oldItem.noteId == newItem.noteId)

    /**
     * Called to check whether two items have the same data.
     *
     * @param oldItem The old item.
     * @param newItem The new item.
     * @return `true` if the items have the same content, `false` otherwise.
     */
    override fun areContentsTheSame(oldItem: Note, newItem: Note) = (oldItem == newItem)
}
