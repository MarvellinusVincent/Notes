package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.notes.databinding.FragmentSignInBinding

/**
 * Fragment for user sign-in and authentication.
 */
class SignInFragment : Fragment() {
    /** Tag for logging purposes. */
    val TAG = "SignInFragment"

    /** Binding for the fragment. */
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    /**
     * Inflates the fragment's layout and sets up the UI components.
     *
     * @param inflater The LayoutInflater object to inflate views.
     * @param container The parent view to attach the fragment's UI.
     * @param savedInstanceState The saved state of the fragment.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root

        /** Get the shared ViewModel for handling user authentication. */
        val viewModel: NotesViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        /** Observe to navigate to the notes fragment. */
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController().navigate(R.id.action_signInFragment_to_notesFragment)
                viewModel.onNavigatedToList()
            }
        })

        /** Observe to navigate to the sign up fragment. */
        viewModel.navigateToSignUp.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
                viewModel.onNavigatedToSignUp()
            }
        })

        /** Observe to show error toast. */
        viewModel.errorHappened.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
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
