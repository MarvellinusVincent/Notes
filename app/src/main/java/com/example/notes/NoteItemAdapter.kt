package com.example.notes

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.databinding.NoteItemBinding

/**
 * Adapter for displaying a list of Note items in a RecyclerView.
 *
 * @param clickListener Function to handle item click events.
 * @param deleteClickListener Function to handle item delete events.
 */
class NoteItemAdapter(
    val clickListener: (note: Note) -> Unit,
) : ListAdapter<Note, NoteItemAdapter.NoteItemViewHolder>(NoteDiffItemCallback()) {

    /**
     * Creates a new NoteItemViewHolder by inflating its layout.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The view type.
     * @return A new NoteItemViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemViewHolder =
        NoteItemViewHolder.inflateFrom(parent)

    /**
     * Binds data to the NoteItemViewHolder and sets up click listeners.
     *
     * @param holder The NoteItemViewHolder to bind data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
        Log.d("NoteItemAdapter", "Note Content: ${item.noteId}")
    }

    /**
     * ViewHolder class for displaying individual Note items.
     *
     * @param binding The data binding for the Note item view.
     */
    class NoteItemViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            /**
             * Inflates the NoteItemViewHolder layout from a parent ViewGroup.
             *
             * @param parent The parent ViewGroup to inflate the layout into.
             * @return A new NoteItemViewHolder.
             */
            fun inflateFrom(parent: ViewGroup): NoteItemViewHolder {
                Log.d("NoteItemViewHolder", "Note inflated")
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NoteItemBinding.inflate(layoutInflater, parent, false)
                return NoteItemViewHolder(binding)
            }
        }

        /**
         * Binds a Note item to the ViewHolder and sets up click listeners.
         *
         * @param item The Note item to bind.
         * @param clickListener Function to handle item click events.
         * @param deleteClickListener Function to handle item delete events.
         */
        fun bind(
            item: Note,
            clickListener: (note: Note) -> Unit,
        ) {
            Log.d("NoteItemAdapter bind", "Note bound")
            binding.note = item
            binding.root.setOnClickListener { clickListener(item) }
        }
    }
}
