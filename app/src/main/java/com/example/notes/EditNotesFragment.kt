package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.notes.databinding.FragmentEditNotesBinding

/**
 * A Fragment for editing notes.
 */
class EditNotesFragment : Fragment() {
    private var _binding: FragmentEditNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /**
         * Inflate the layout for this fragment using data binding.
         */
        _binding = FragmentEditNotesBinding.inflate(inflater, container, false)
        val view = binding.root

        /**
         * Get the noteId from the arguments passed to this fragment.
         */
        val noteId = EditNotesFragmentArgs.fromBundle(requireArguments()).noteId

        /**
         * Get application and DAO instances for the ViewModelFactory.
         */
        val application = requireNotNull(this.activity).application
        val dao = NoteDatabase.getInstance(application).noteDao

        /**
         * Create a ViewModelFactory with the noteId and DAO.
         */
        val viewModelFactory = EditNotesViewModelFactory(noteId, dao)

        /**
         * Create the ViewModel with the ViewModelFactory.
         */
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditNotesViewModel::class.java)

        /**
         * Bind the ViewModel to the layout.
         */
        binding.viewModel = viewModel

        /**
         * Set the lifecycle owner for observing LiveData.
         */
        binding.lifecycleOwner = viewLifecycleOwner

        /**
         * Observe the navigateToList LiveData to handle navigation.
         */
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_editNotesFragment_to_notesFragment)
                viewModel.onNavigatedToList()
            }
        })
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**
         * Clean up the binding reference when the view is destroyed.
         */
        _binding = null
    }
}
