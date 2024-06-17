package com.example.notes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.databinding.FragmentNotesBinding

/**
 * Fragment for displaying a list of notes and providing note management functionality.
 */
class NotesFragment : Fragment() {

    /**  Binding for Log Tag. */
    val TAG = "NotesFragment"

    /**  Binding for the fragment. */
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    /**
     * Inflates the fragment's layout and sets up the UI components.
     *
     * @param inflater The LayoutInflater object to inflate views.
     * @param container The parent view to attach the fragment's UI.
     * @param savedInstanceState The saved state of the fragment.
     * @return The root view of the fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = binding.root

        /** Get the shared ViewModel for handling notes. */
        val viewModel : NotesViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        /** Function to handle when a note is clicked. */
        fun noteClicked(note: Note) {
            viewModel.onNoteClicked(note)
        }

        /** Function to handle "Yes" button press in delete confirmation. */
        fun yesPressed(noteId: String) {
            Log.d(TAG, "in yesPressed(): noteId = $noteId")
            binding.viewModel?.deleteNote(noteId)
        }

        /** Function to handle when the delete button is clicked for a note. */
        fun deleteClicked(noteId: String) {
            ConfirmDeleteDialogFragment(noteId, ::yesPressed)
                .show(childFragmentManager, ConfirmDeleteDialogFragment.TAG)
        }

        /** Observing the change in the notes database and updates the recycler view. */
        val adapter = NoteItemAdapter(::noteClicked)
        binding.listOfNotes.adapter = adapter
        viewModel.notes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                Log.d("Notes Fragment", "New note is added. List size: ${it.size}")
            }
        })

        /** Observes to navigate to the edit notes fragment. */
        viewModel.navigateToNote.observe(viewLifecycleOwner, Observer { noteId ->
            noteId?.let {
                Log.d("NotesFragment", "addNoteId observed: $noteId")
                val action = NotesFragmentDirections
                    .actionNotesFragmentToEditNotesFragment(noteId)
                this.findNavController().navigate(action)
                viewModel.onNoteNavigated()
            }
        })

        /** Observes to navigate to the sign in fragment. */
        viewModel.navigateToSignIn.observe(viewLifecycleOwner, Observer { navigate ->
            if(navigate) {
                this.findNavController().navigate(R.id.action_notesFragment_to_signInFragment)
                viewModel.onNavigatedToSignIn()
            }
        })
        return view
    }
    /**
     * Cleans up the binding when the fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}