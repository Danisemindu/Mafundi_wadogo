package com.example.api

data class User(
    val firstName: String = "",
    val secondName: String = "",
    val location: String = "",
    val gender: String = "",
    val registrationType: String = "",
    val handymanType: String = "",
    val number: String = "",
    val specificSkills: String = "",
    var hireCount: Int = 0
)
