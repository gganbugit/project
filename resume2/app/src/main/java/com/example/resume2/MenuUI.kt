package com.example.resume2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.resume2.Resume.ResumeEx
import com.example.resume2.Resume.ResumeUI
import com.example.resume2.TTS.TTEx
import com.example.resume2.TTS.TTSUI
import com.example.resume2.logout.MyPageUI
import kotlinx.android.synthetic.main.menuui.*

class MenuUI : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menuui)

        var MyPageIntent = Intent(this, MyPageUI::class.java)
        var CoverIntent = Intent(this, ResumeEx::class.java)
        var InterviewIntent = Intent(this, TTEx::class.java)

        CoverBtn.setOnClickListener()
        {
            startActivity(CoverIntent)
        }

        InterviewBtn.setOnClickListener()
        {
            startActivity(InterviewIntent)
        }

        MyPageBtn.setOnClickListener()
        {
            startActivity(MyPageIntent)
        }
    }
}