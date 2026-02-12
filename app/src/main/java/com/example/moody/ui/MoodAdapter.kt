package com.example.moody.ui

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moody.R
import com.example.moody.data.MoodEntry

class MoodAdapter : RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    private var moods = emptyList<MoodEntry>()

    class MoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moodEmoji: TextView = itemView.findViewById(R.id.moodEmoji)
        val moodType: TextView = itemView.findViewById(R.id.moodType)
        val moodTimestamp: TextView = itemView.findViewById(R.id.moodTimestamp)
        val moodNote: TextView = itemView.findViewById(R.id.moodNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mood, parent, false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val mood = moods[position]

        holder.moodEmoji.text = mood.emoji
        holder.moodType.text = formatMoodType(mood.moodType)

        // Format timestamp (e.g., "2 hours ago")
        val timeAgo = DateUtils.getRelativeTimeSpanString(
            mood.timestamp,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        )
        holder.moodTimestamp.text = timeAgo

        // Show note if exists, hide if empty
        if (mood.note.isNotEmpty()) {
            holder.moodNote.visibility = View.VISIBLE
            holder.moodNote.text = mood.note
        } else {
            holder.moodNote.visibility = View.GONE
        }
    }

    override fun getItemCount() = moods.size

    fun setMoods(moods: List<MoodEntry>) {
        this.moods = moods
        notifyDataSetChanged()
    }

    private fun formatMoodType(type: String): String {
        return type.replace("_", " ").split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }
}
