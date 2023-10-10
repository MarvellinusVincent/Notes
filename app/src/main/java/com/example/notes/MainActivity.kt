package com.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * The main activity of the application.
 */
class MainActivity : AppCompatActivity() {
    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState A Bundle containing the saved state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the main layout resource.
        setContentView(R.layout.activity_main)
    }
}
