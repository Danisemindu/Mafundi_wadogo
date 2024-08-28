// Feedback.kt
package com.example.api

data class Feedback(
    val handymanId: String = "",
    val handymanName: String = "",
    val handymanType: String = "",
    val rating: Float = 0f,
    val feedback: String = ""
)
