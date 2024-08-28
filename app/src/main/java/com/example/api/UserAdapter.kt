package com.example.api

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class UserAdapter(private var userList: List<User>, private val context: Context) : RecyclerView.Adapter<UserAdapter.UserViewHolder>(), Filterable {

    private var filteredUserList: List<User> = userList

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        val userLocationTextView: TextView = itemView.findViewById(R.id.userLocationTextView)
        val userSkillsTextView: TextView = itemView.findViewById(R.id.userSkillsTextView)
        val userHireCountTextView: TextView = itemView.findViewById(R.id.userHireCountTextView)
        val handymanImageView: ImageView = itemView.findViewById(R.id.profile)
        val callButton: Button = itemView.findViewById(R.id.callButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = filteredUserList[position]
        holder.userNameTextView.text = "${user.firstName} ${user.secondName}"
        holder.userLocationTextView.text = user.location
        holder.userSkillsTextView.text = "Skills: ${user.specificSkills}"
        holder.userHireCountTextView.text = "Hires: ${user.hireCount}"

        // Set the image based on handyman type
        val imageResId = R.drawable.profile
        holder.handymanImageView.setImageResource(imageResId)

        holder.callButton.setOnClickListener {
            val phoneNumber = user.number
            if (phoneNumber.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                context.startActivity(intent)
                showRatingDialog(user)
                incrementHireCount(user)
            } else {
                Toast.makeText(context, "Phone number not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredUserList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredUserList = if (charString.isEmpty()) {
                    userList
                } else {
                    userList.filter {
                        it.firstName.contains(charString, true) ||
                                it.secondName.contains(charString, true) ||
                                it.location.contains(charString, true) ||
                                it.handymanType.contains(charString, true)
                    }
                }
                return FilterResults().apply { values = filteredUserList }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredUserList = if (results?.values == null) {
                    listOf()
                } else {
                    results.values as List<User>
                }
                notifyDataSetChanged()
            }
        }
    }

    private fun showRatingDialog(user: User) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.activity_dialog)

        val ratingBar: RatingBar = dialog.findViewById(R.id.ratingBar)
        val feedbackEditText: EditText = dialog.findViewById(R.id.feedbackEditText)
        val submitButton: Button = dialog.findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val rating = ratingBar.rating
            val feedback = feedbackEditText.text.toString()

            if (rating > 0 && feedback.isNotEmpty()) {
                saveFeedback(user, rating, feedback)
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Please provide both rating and feedback", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    //feedback database
    private fun saveFeedback(user: User, rating: Float, feedback: String) {
        val database = FirebaseDatabase.getInstance().reference
        val feedbackMap = mapOf(
            "handymanId" to user.number,
            "handymanName" to "${user.firstName} ${user.secondName}",
            "handymanType" to user.handymanType,
            "rating" to rating,
            "feedback" to feedback
        )

        database.child("feedbacks").push().setValue(feedbackMap)
            .addOnSuccessListener {
                Toast.makeText(context, "Feedback submitted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to submit feedback", Toast.LENGTH_SHORT).show()
            }
    }

    private fun incrementHireCount(user: User) {
        val database = FirebaseDatabase.getInstance().reference
        val userRef = database.child("users").child(user.number)

        userRef.child("hireCount").setValue(user.hireCount + 1)
            .addOnSuccessListener {
                user.hireCount += 1
                notifyDataSetChanged()
                Toast.makeText(context, "Hire count updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update hire count", Toast.LENGTH_SHORT).show()
            }
    }
}
