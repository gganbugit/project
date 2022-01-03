package com.example.resume2.logout

data class MyPageView (
    var covers: ArrayList<cover>,
    var cover : ArrayList<DetailCover>,
    var interviews : ArrayList<TTSMember>,
    var interview : ArrayList<interview>,
    var content: String,
    var cover_id: Int,
    var subject: String,
    var user_id: String,
    var cover_count: Int,
    var interview_count : Int,
    var interview_id: Int,
    var question: String,
    var answer: String
)


