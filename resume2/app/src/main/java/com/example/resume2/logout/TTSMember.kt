package com.example.resume2.logout

data class TTSMember (
    val interview_count: Int,
    val interview_id: Int,
    val question: String,
    val answer: String,
    val user_id : String,
    val cover_count : Int
)

data class interview (
    var question: String,
    var answer : String
)