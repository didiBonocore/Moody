package com.example.moody

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MoodDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_detail)

        // Retrieve mood data from the explicit intent extras
        val emoji = intent.getStringExtra("emoji") ?: ""
        val moodType = intent.getStringExtra("moodType") ?: ""
        val note = intent.getStringExtra("note") ?: ""
        val timestamp = intent.getLongExtra("timestamp", 0L)

        // Format mood type: "very_happy" → "Very Happy"
        val formattedType = moodType.replace("_", " ").split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }

        // Format timestamp as relative time
        val timeAgo = DateUtils.getRelativeTimeSpanString(
            timestamp,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        )

        // Populate the detail views
        findViewById<TextView>(R.id.detailEmoji).text = emoji
        findViewById<TextView>(R.id.detailMoodType).text = formattedType
        findViewById<TextView>(R.id.detailTimestamp).text = timeAgo

        val noteLabel = findViewById<TextView>(R.id.detailNoteLabel)
        val noteText = findViewById<TextView>(R.id.detailNote)
        if (note.isNotEmpty()) {
            noteLabel.visibility = View.VISIBLE
            noteText.visibility = View.VISIBLE
            noteText.text = note
        } else {
            noteLabel.visibility = View.GONE
            noteText.visibility = View.GONE
        }

        // Implicit intent: share mood with other apps
        findViewById<Button>(R.id.btnShare).setOnClickListener {
            val shareText = buildString {
                append("I'm feeling $formattedType $emoji")
                if (note.isNotEmpty()) append("\n\n$note")
                append("\n\n— Shared from Moody")
            }
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(shareIntent, "Share your mood"))
        }
    }
}
