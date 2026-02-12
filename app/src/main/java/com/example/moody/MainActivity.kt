package com.example.moody

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moody.data.MoodEntry
import com.example.moody.ui.MoodAdapter
import com.example.moody.viewmodel.MoodViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var moodViewModel: MoodViewModel
    private lateinit var adapter: MoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.moodRecyclerView)
        adapter = MoodAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up ViewModel
        moodViewModel = ViewModelProvider(this)[MoodViewModel::class.java]

        // Observe mood data (auto-updates UI when data changes)
        moodViewModel.allMoods.observe(this) { moods ->
            adapter.setMoods(moods)
        }

        // Set up FAB click listener
        val fab = findViewById<FloatingActionButton>(R.id.fabAddMood)
        fab.setOnClickListener {
            showAddMoodDialog()
        }
    }

    private fun showAddMoodDialog() {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_add_mood, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Get references to views
        val btnVeryHappy = dialogView.findViewById<Button>(R.id.btnVeryHappy)
        val btnHappy = dialogView.findViewById<Button>(R.id.btnHappy)
        val btnNeutral = dialogView.findViewById<Button>(R.id.btnNeutral)
        val btnSad = dialogView.findViewById<Button>(R.id.btnSad)
        val btnVerySad = dialogView.findViewById<Button>(R.id.btnVerySad)
        val etNote = dialogView.findViewById<EditText>(R.id.etNote)

        // Set up click listeners for each mood button
        btnVeryHappy.setOnClickListener {
            saveMood("very_happy", "😄", etNote.text.toString())
            dialog.dismiss()
        }

        btnHappy.setOnClickListener {
            saveMood("happy", "😊", etNote.text.toString())
            dialog.dismiss()
        }

        btnNeutral.setOnClickListener {
            saveMood("neutral", "😐", etNote.text.toString())
            dialog.dismiss()
        }

        btnSad.setOnClickListener {
            saveMood("sad", "😟", etNote.text.toString())
            dialog.dismiss()
        }

        btnVerySad.setOnClickListener {
            saveMood("very_sad", "😢", etNote.text.toString())
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveMood(moodType: String, emoji: String, note: String) {
        val mood = MoodEntry(
            moodType = moodType,
            emoji = emoji,
            note = note.trim()
        )
        moodViewModel.insert(mood)
    }
}
