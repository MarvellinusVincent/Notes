package com.example.notes

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

class NotesViewModel: ViewModel() {
    private lateinit var auth: FirebaseAuth
    private lateinit var notesCollection: DatabaseReference

    var user: User = User()
    var note = MutableLiveData<Note>()
    var noteId: String = ""
    var verifyPassword = ""

    private val _notes: MutableLiveData<MutableList<Note>> = MutableLiveData()
    private val _navigateToNote = MutableLiveData<String?>()
    private val _navigateToList = MutableLiveData<Boolean>()
    private val _navigateToSignUp = MutableLiveData<Boolean>(false)
    private val _navigateToSignIn = MutableLiveData<Boolean>(false)
    private val _errorHappened = MutableLiveData<String?>()

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

    init {
        auth = Firebase.auth
        if (noteId.trim() == "") {
            note.value = Note()
        }
        _notes.value = mutableListOf<Note>()
        val database = Firebase.database
        _notes.value = mutableListOf<Note>()
        notesCollection = database.getReference("notes")
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val note = dataSnapshot.getValue<Note>()
                _notes.value!!.add(note!!)
            }
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val newNote = dataSnapshot.getValue<Note>()
                val noteKey = dataSnapshot.key
            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val noteKey = dataSnapshot.key
            }
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val movedNote = dataSnapshot.getValue<Note>()
                val noteKey = dataSnapshot.key
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        notesCollection.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var notesList : ArrayList<Note> = ArrayList()
                for (noteSnapshot in dataSnapshot.children) {
                    var note = noteSnapshot.getValue<Note>()
                    note?.noteId = noteSnapshot.key!!
                    notesList.add(note!!)
                }
                _notes.value = notesList
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

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
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
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
    fun signOut() {
        auth.signOut()
        _navigateToList.value = true
    }
    fun getAll(): LiveData<List<Note>> {
        return notes
    }
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
    fun saveNote() {
        if (noteId.trim() == "") {
            notesCollection.push().setValue(note.value)
        } else {
            notesCollection.child(noteId).setValue(note.value)
        }
        _navigateToList.value = true
    }
    fun deleteNote(noteId: String) {
        notesCollection.child(noteId).removeValue()
    }
    fun onNoteClicked(selectedNote: Note) {
        _navigateToNote.value = selectedNote.noteId
        noteId = selectedNote.noteId
        note.value = selectedNote
    }
    fun onNewNoteClicked() {
        _navigateToNote.value = ""
        noteId = ""
        note.value = Note()
    }
    fun onNoteNavigated() {
        _navigateToNote.value = null
    }
    fun onNavigatedToList() {
        _navigateToList.value = false
    }
    fun navigateToSignUp() {
        _navigateToSignUp.value = true
    }
    fun onNavigatedToSignUp() {
        _navigateToSignUp.value = false
    }
    fun navigateToSignIn() {
        _navigateToSignIn.value = true
    }
    fun onNavigatedToSignIn() {
        _navigateToSignIn.value = false
    }
}
