package com.example.notes

/**
 * Data class representing a user with email and password.
 *
 * @property email The email address of the user.
 * @property password The password associated with the user's account.
 */
data class User(
    var email: String = "",
    var password: String = ""
)
