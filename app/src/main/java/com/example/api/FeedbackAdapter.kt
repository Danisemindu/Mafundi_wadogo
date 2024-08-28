// FeedbackAdapter.kt
package com.example.api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeedbackAdapter(private val feedbackList: List<Feedback>) : RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {

    class FeedbackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val handymanNameTextView: TextView = itemView.findViewById(R.id.handymanNameTextView)
        val handymanTypeTextView: TextView = itemView.findViewById(R.id.handymanTypeTextView)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val feedbackTextView: TextView = itemView.findViewById(R.id.feedbackTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feedback, parent, false)
        return FeedbackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val feedback = feedbackList[position]
        holder.handymanNameTextView.text = feedback.handymanName
        holder.handymanTypeTextView.text = feedback.handymanType
        holder.ratingBar.rating = feedback.rating
        holder.feedbackTextView.text = feedback.feedback
    }

    override fun getItemCount(): Int {
        return feedbackList.size
    }
}
