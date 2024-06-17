package com.example.notes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

/**
 * ViewModel for managing user notes and authentication.
 */
class NotesViewModel : ViewModel() {
    /** Firebase Authentication instance. */
    private lateinit var auth: FirebaseAuth

    /** DatabaseReference for notes data. */
    private lateinit var notesCollection: DatabaseReference

    /** User data. */
    var user: User = User()

    /** Current note data. */
    var note = MutableLiveData<Note>()

    /** Current note's ID. */
    var noteId: String = ""

    /** Password verification. */
    var verifyPassword = ""

    /** MutableLiveData properties for LiveData. */
    private val _notes: MutableLiveData<MutableList<Note>> = MutableLiveData()
    private val _navigateToNote = MutableLiveData<String?>()
    private val _navigateToList = MutableLiveData<Boolean>(false)
    private val _navigateToSignUp = MutableLiveData<Boolean>(false)
    private val _navigateToSignIn = MutableLiveData<Boolean>(false)
    private val _errorHappened = MutableLiveData<String?>()

    /** LiveData properties accessible to the view. */
    val notes: LiveData<List<Note>>
        get() = _notes as LiveData<List<Note>>
    val navigateToNote: LiveData<String?>
        get() = _navigateToNote
    val navigateToList: LiveData<Boolean>
        get() = _navigateToList
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp
    val navigateToSignIn: LiveData<Boolean>
        get() = _navigateToSignIn
    val errorHappened: LiveData<String?>
        get() = _errorHappened

    // Constructor for initialization.
    init {
        auth = Firebase.auth
        if (noteId.trim() == "") {
            note.value = Note()
        }
        _notes.value = mutableListOf<Note>()
    }

    /**
     * Initialize the DatabaseReference for accessing the notes data in Firebase.
     */
    fun initializeTheDatabaseReference() {
        val database = Firebase.database
        notesCollection = database
            .getReference("notes")
            .child(auth.currentUser!!.uid)
        notesCollection.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var notesList: ArrayList<Note> = ArrayList()
                for (noteSnapshot in dataSnapshot.children) {
                    var note = noteSnapshot.getValue<Note>()
                    note?.noteId = noteSnapshot.key!!
                    notesList.add(note!!)
                }
                _notes.value = notesList
                Log.d("NotesViewModel", "Notes retrieved successfully: ${notesList.size} notes")
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    /**
     * Sign in with Firebase Authentication.
     */
    fun signIn() {
        if (user.email.isEmpty() || user.password.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }
        auth.signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                initializeTheDatabaseReference()
                _navigateToList.value = true
            } else {
                _errorHappened.value = it.exception?.message
            }
        }
    }

    /**
     * Sign up a new user with Firebase Authentication.
     */
    fun signUp() {
        if (user.email.isEmpty() || user.password.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }
        if (user.password != verifyPassword) {
            _errorHappened.value = "Password and verify do not match."
            return
        }
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                _navigateToSignIn.value = true
            } else {
                _errorHappened.value = it.exception?.message
            }
        }
    }

    /**
     * Sign out the current user.
     */
    fun signOut() {
        auth.signOut()
        _notes.value = mutableListOf()
        _navigateToList.value = true
    }

    /**
     * Get a LiveData object of notes.
     */
    fun getAll(): LiveData<List<Note>> {
        return notes
    }

    /**
     * Get the current user's FirebaseUser.
     */
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    /**
     * Save a note to Firebase.
     */
    fun saveNote() {
        if (noteId.trim() == "") {
            initializeTheDatabaseReference()
            notesCollection.push().setValue(note.value)
        } else {
            initializeTheDatabaseReference()
            notesCollection.child(noteId).setValue(note.value)
        }
        _navigateToList.value = true
    }

    /**
     * Delete a note from Firebase.
     */
    fun deleteNote(noteId: String) {
        notesCollection.child(noteId).removeValue()
        _navigateToList.value = true
    }

    /**
     * Handle navigation to a specific note.
     */
    fun onNoteClicked(selectedNote: Note) {
        _navigateToNote.value = selectedNote.noteId
        noteId = selectedNote.noteId
        note.value = selectedNote
    }

    /**
     * Handle navigation to create a new note.
     */
    fun onNewNoteClicked() {
        _navigateToNote.value = ""
        noteId = ""
        note.value = Note()
    }

    /**
     * Handle when note navigation is completed.
     */
    fun onNoteNavigated() {
        _navigateToNote.value = null
    }

    /**
     * Handle when navigation to the list is completed.
     */
    fun onNavigatedToList() {
        _navigateToList.value = false
    }

    /**
     * Navigate to the sign-up screen.
     */
    fun navigateToSignUp() {
        _navigateToSignUp.value = true
    }

    /**
     * Handle when navigation to sign-up is completed.
     */
    fun onNavigatedToSignUp() {
        _navigateToSignUp.value = false
    }

    /**
     * Navigate to the sign-in screen.
     */
    fun navigateToSignIn() {
        _navigateToSignIn.value = true
    }

    /**
     * Handle when navigation to sign-in is completed.
     */
    fun onNavigatedToSignIn() {
        _navigateToSignIn.value = false
    }
}
