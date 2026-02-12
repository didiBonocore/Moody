package com.example.moody

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moody.data.MoodEntry
import com.example.moody.ui.MoodAdapter
import com.example.moody.viewmodel.MoodViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var moodViewModel: MoodViewModel
    private lateinit var adapter: MoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up RecyclerView with click listener (explicit intent to detail activity)
        val recyclerView = findViewById<RecyclerView>(R.id.moodRecyclerView)
        adapter = MoodAdapter { mood ->
            val intent = Intent(this, MoodDetailActivity::class.java).apply {
                putExtra("emoji", mood.emoji)
                putExtra("moodType", mood.moodType)
                putExtra("note", mood.note)
                putExtra("timestamp", mood.timestamp)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up swipe-to-delete
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val mood = adapter.getMoodAt(position)
                moodViewModel.delete(mood)
                Snackbar.make(recyclerView, "Mood deleted", Snackbar.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(recyclerView)

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

        // Set up reminder toggle button
        val btnReminder = findViewById<Button>(R.id.btnReminder)
        updateReminderButton(btnReminder)
        btnReminder.setOnClickListener {
            if (ReminderScheduler.isReminderEnabled(this)) {
                ReminderScheduler.cancel(this)
            } else {
                ReminderScheduler.schedule(this)
            }
            updateReminderButton(btnReminder)
        }

        // Fetch motivational quote from API (networking)
        fetchQuote()
    }

    private fun updateReminderButton(button: Button) {
        if (ReminderScheduler.isReminderEnabled(this)) {
            button.text = "Reminder: ON"
        } else {
            button.text = "Reminder: OFF"
        }
    }

    private fun fetchQuote() {
        val quoteText = findViewById<TextView>(R.id.quoteText)
        val quoteAuthor = findViewById<TextView>(R.id.quoteAuthor)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://zenquotes.io/api/random")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val response = connection.inputStream.bufferedReader().readText()
                connection.disconnect()

                val jsonArray = JSONArray(response)
                val quoteObj = jsonArray.getJSONObject(0)
                val quote = quoteObj.getString("q")
                val author = quoteObj.getString("a")

                withContext(Dispatchers.Main) {
                    quoteText.text = "\"$quote\""
                    quoteAuthor.text = "— $author"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    quoteText.text = "\"Every day is a fresh start.\""
                    quoteAuthor.text = "— Unknown"
                }
            }
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
