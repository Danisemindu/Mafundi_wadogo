package com.example.api

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Tailor : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: MutableList<User>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tailor)

        database = FirebaseDatabase.getInstance().reference.child("users")
        userList = mutableListOf()
        userAdapter = UserAdapter(userList, this) // Pass context to the adapter
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter

        fetchUsers()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userAdapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun fetchUsers() {
        database.orderByChild("handymanType").equalTo("Tailor")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null) {
                            userList.add(user)
                        }
                    }
                    userAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle possible errors.
                }
            })
    }
}
