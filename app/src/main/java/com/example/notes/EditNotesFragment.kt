package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.notes.databinding.FragmentEditNotesBinding

/**
 * A fragment for editing notes.
 */
class EditNotesFragment : Fragment() {
    private var _binding: FragmentEditNotesBinding? = null
    private val binding get() = _binding!!

    /**
     * Inflates the fragment's layout and handles note editing.
     *
     * @param inflater           The LayoutInflater object for inflating views.
     * @param container          The parent view to attach the fragment's UI.
     * @param savedInstanceState  The saved state of the fragment.
     * @return The root view of the fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditNotesBinding.inflate(inflater, container, false)
        val view = binding.root
        val noteId = EditNotesFragmentArgs.fromBundle(requireArguments()).noteId
        val viewModel : NotesViewModel by activityViewModels()
        viewModel.noteId = noteId
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_editNotesFragment_to_notesFragment)
                viewModel.onNavigatedToList()
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
