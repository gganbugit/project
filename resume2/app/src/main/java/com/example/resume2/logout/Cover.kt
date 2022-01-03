package com.example.resume2.logout

data class cover(
    var content: String,
    var cover_id: Int,
    var subject: String,
    var user_id: String,
    var cover_count: Int,
    var interview_count : Int
)

data class DetailCover(
    var subject: String,
    var content: String
)



