// DashboardActivity.kt
package com.example.api

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Dashboard : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var feedbackAdapter: FeedbackAdapter
    private lateinit var feedbackList: MutableList<Feedback>
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        database = FirebaseDatabase.getInstance().reference.child("feedbacks")
        feedbackList = mutableListOf()
        feedbackAdapter = FeedbackAdapter(feedbackList)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = feedbackAdapter

        fetchFeedbacks()
    }

    private fun fetchFeedbacks() {
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                feedbackList.clear()
                for (feedbackSnapshot in snapshot.children) {
                    val feedback = feedbackSnapshot.getValue(Feedback::class.java)
                    if (feedback != null) {
                        feedbackList.add(feedback)
                    }
                }
                feedbackAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
}
