package com.example.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.databinding.NoteItemBinding

/**
 * Adapter class for displaying a list of notes in a RecyclerView.
 *
 * @param data The list of notes to be displayed.
 * @param clickListener A lambda function to handle item clicks.
 * @param deleteClickListener A lambda function to handle delete button clicks.
 */
class NoteItemAdapter(
    private val data: List<Note>,
    private val clickListener: (noteId: Long) -> Unit,
    private val deleteClickListener: (noteId: Long) -> Unit
) : RecyclerView.Adapter<NoteItemAdapter.NoteItemViewHolder>() {

    /**
     * List of notes to be displayed.
     */
    var notes: MutableList<Note> = data.toMutableList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * ViewHolder class for individual note items.
     *
     * @param binding The data binding object for the item view.
     */
    class NoteItemViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            /**
             * Create a new instance of the NoteItemViewHolder from the provided parent ViewGroup.
             *
             * @param parent The parent ViewGroup.
             * @return A new NoteItemViewHolder instance.
             */
            fun inflateFrom(parent: ViewGroup): NoteItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NoteItemBinding.inflate(layoutInflater, parent, false)
                return NoteItemViewHolder(binding)
            }
        }

        /**
         * Bind data to the item view and set click listeners.
         *
         * @param item The Note object to bind.
         * @param clickListener A lambda function to handle item clicks.
         * @param deleteClickListener A lambda function to handle delete button clicks.
         */
        fun bind(item: Note, clickListener: (noteId: Long) -> Unit, deleteClickListener: (noteId: Long) -> Unit) {
            binding.note = item
            binding.root.setOnClickListener { clickListener(item.noteId) }
            binding.deleteButton.setOnClickListener { deleteClickListener(item.noteId) }
        }
    }

    /**
     * Create and return a new instance of NoteItemViewHolder.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The view type.
     * @return A new NoteItemViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemViewHolder {
        return NoteItemViewHolder.inflateFrom(parent)
    }

    /**
     * Bind data to the ViewHolder and specify click listeners.
     *
     * @param holder The NoteItemViewHolder to bind data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        val item = notes[position]
        holder.bind(item, clickListener, deleteClickListener)
    }

    /**
     * Get the total number of items in the list.
     *
     * @return The total number of items in the list.
     */
    override fun getItemCount(): Int {
        return notes.size
    }
}
