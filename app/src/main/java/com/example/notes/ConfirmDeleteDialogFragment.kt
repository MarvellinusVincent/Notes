package com.example.notes

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment

/**
 * A DialogFragment used to confirm the deletion of a note.
 *
 * @param noteId The ID of the note to be deleted.
 * @param clickListener A lambda function that will be called when the user confirms the deletion.
 */
class ConfirmDeleteDialogFragment(val noteId: Long, val clickListener: (noteId: Long) -> Unit) : DialogFragment() {

    /**
     * Tag for logging purposes
     */
    val TAG = "ConfirmDeleteDialogFragment"

    /**
     * Interface for handling the "yes" button click event.
     */
    interface myClickListener {
        /**
         * Called when the "yes" button is pressed.
         */
        fun yesPressed()
    }

    /**
     * Reference to the listener for "yes" button click event
     */
    var listener: myClickListener? = null

    /**
     * Creates and returns the confirmation delete dialog.
     *
     * @param savedInstanceState The saved instance state of the fragment.
     * @return The created AlertDialog for confirming delete.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.delete_confirmation))
            .setPositiveButton(getString(R.string.yes)) { _, _ -> clickListener(noteId) }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .create()

    companion object {
        const val TAG = "ConfirmDeleteDialogFragment"
    }

    /**
     * Called when the fragment is attached to a context.
     *
     * @param context The context to which the fragment is attached.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as myClickListener
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }
}
