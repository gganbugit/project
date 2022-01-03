package com.example.resume2.Resume

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.resume2.R
import kotlinx.android.synthetic.main.resumeex.*

class ResumeEx : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resumeex)

        clearResumeBtn.setOnClickListener()
        {
            val intent = Intent(this, ResumeUI::class.java)
            startActivity(intent)
        }

        // 자기소개서 튜토리얼 화면으로, X 버튼을 누르면 ResumeUI로 화면 전환
    }
}