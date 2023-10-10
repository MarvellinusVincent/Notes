package com.example.notes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.databinding.FragmentNotesBinding

/**
 * A fragment that displays a list of notes and handles user interactions.
 */
class NotesFragment : Fragment() {

    /**
     * Tag for logging purposes
     */
    val TAG = "NotesFragment"

    /**
     * View binding for the fragment
     */
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    /**
     * Inflates the fragment's layout and initializes necessary components.
     *
     * @param inflater The LayoutInflater to inflate the layout.
     * @param container The parent view that the fragment UI should be attached to.
     * @param savedInstanceState The saved instance state, if any.
     * @return The root View of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = binding.root

        /**
         * Initialize application, DAO, and ViewModel
         */
        val application = requireNotNull(this.activity).application
        val dao = NoteDatabase.getInstance(application).noteDao
        val viewModelFactory = NotesViewModelFactory(dao)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(NotesViewModel::class.java)

        /**
         * Bind ViewModel to the layout
         */
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        /**
         * Function to handle note item click
         */
        fun noteClicked(noteId: Long) {
            viewModel.onNoteClicked(noteId)
        }

        /**
         * Function to handle "Yes" button click in delete confirmation dialog
         */
        fun yesPressed(noteId: Long) {
            Log.d(TAG, "in yesPressed(): noteId = $noteId")
            viewModel.deleteNote(noteId)
        }

        /**
         * Function to handle delete button click
         */
        fun deleteClicked(noteId: Long) {
            ConfirmDeleteDialogFragment(noteId, ::yesPressed)
                .show(childFragmentManager, ConfirmDeleteDialogFragment.TAG)
        }

        /**
         * Initialize RecyclerView adapter and layout manager
         */
        val adapter = NoteItemAdapter(emptyList(), ::noteClicked, ::deleteClicked)
        binding.listOfNotes.adapter = adapter
        binding.listOfNotes.layoutManager = LinearLayoutManager(requireContext())

        /**
         * Observe changes in the list of notes
         */
        viewModel.notes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.notes = it.toMutableList()
            }
        })

        /**
         * Navigate to the EditNotesFragment when a note is clicked
         */
        viewModel.navigateToNote.observe(viewLifecycleOwner, Observer { noteId ->
            noteId?.let {
                Log.d("NotesFragment", "addNoteId observed: $noteId")
                val action = NotesFragmentDirections
                    .actionNotesFragmentToEditNotesFragment(noteId)
                this.findNavController().navigate(action)
                viewModel.onNoteNavigated()
            }
        })

        return view
    }

    /**
     * Cleans up the view binding when the fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
