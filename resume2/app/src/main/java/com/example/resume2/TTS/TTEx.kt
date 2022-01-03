package com.example.resume2.TTS

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.resume2.R
import kotlinx.android.synthetic.main.ttex.*

class TTEx : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ttex)

        clearTTSBtn.setOnClickListener()
        {
            val intent = Intent(this, TTSUI::class.java)
            startActivity(intent)
        }

        // 모의면접 튜토리얼 화면으로, X 버튼을 누르면 TTSUI로 화면 전환
    }
}