package com.example.resume2.Resume

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import androidx.lifecycle.*
import com.example.resume2.MenuUI
import com.example.resume2.R
import com.example.resume2.TTS.TTEx
import com.example.resume2.TTS.TTSUI
import com.example.resume2.logout.MyPageUI
import com.example.resume2.okhttp_client
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.resumeui.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

class ResumeUI : AppCompatActivity() {
    private lateinit var progressDialog: AppCompatDialog
    var imm: InputMethodManager? = null

    // 가상키보드 숨기기 구현 코드

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resumeui)

        var MyPageIntent = Intent(this, MyPageUI::class.java)
        var HomeIntent = Intent(this, MenuUI::class.java)
        var InterviewIntent = Intent(this, TTEx::class.java)

        // 버튼 클릭시 화면전환을 위한 인텐트 변수 선언

        fun progressON() {
            progressDialog = AppCompatDialog(this)
            progressDialog.setCancelable(false)
            progressDialog.getWindow()
                ?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            progressDialog.setContentView(R.layout.loading_dialog)
            progressDialog.show()
            var img_loading_framge = progressDialog.findViewById<ImageView>(R.id.loading)
            var frameAnimation = img_loading_framge?.getBackground() as AnimationDrawable
            img_loading_framge?.post(object : Runnable {
                override fun run() {
                    frameAnimation.start()
                }
            })
        }

        // 커스텀 다이얼로그 (로딩화면) 창을 띄우는 함수 선언

        fun progressOFF() {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss()
            }
        }

        // 커스텀 다이얼로그 (로딩화면) 창을 종료하는 함수 선언

        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?

        // 가상키보드 숨기기 구현 코드

        var loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        // 서버와 통신 중 로그를 기록하기 위함

        var gson = GsonBuilder().setLenient().create()

        var client = okhttp_client

        // 클라이언트는 젼역변수로 선언한 클라이언트를 사용

        var retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://52.78.99.20/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        // Retrofit 객체 생성

        var resumeService = retrofit.create(ResumeService::class.java)

        // resumeService Retrofit 생성

        reciveBtn.setOnClickListener {

            // 검색 버튼을 눌렀을 때 발생하는 이벤트 처리

            var insertword = inputWord.text.toString()

            // 입력받은 키워드를 문자열로 변환

            if (insertword != "") {
                progressON()

                resumeService.requestResume(insertword).enqueue(object : Callback<Resume> {
                    override fun onResponse(call: Call<Resume>, response: Response<Resume>) {
                        val result = response.body()
                        result?.let {
                            var recoSentence1 = it.output[0]
                            var recoSentence2 = it.output[1]
                            var recoSentence3 = it.output[2]
                            var recoSentence4 = it.output[3]
                            var recoSentence5 = it.output[4]
                            recomm1.setText(recoSentence1)
                            recomm2.setText(recoSentence2)
                            recomm3.setText(recoSentence3)
                            recomm4.setText(recoSentence4)
                            recomm5.setText(recoSentence5)

                            progressOFF()
                        }
                    }

                    /* 입력받은 단어가 있을 경우 (Null이 아닐 경우) 에 progressOn 함수를 실행하여
                       커스텀 다이얼로그 (로딩 화면) 창을 띄우고 생성된 5개의 문장을 5개의 EditText에 한 문장씩 넣음
                       커스텀 다이얼로그 (로딩 화면) 종료 */

                    override fun onFailure(call: Call<Resume>, t: Throwable) {
                        var dialog = AlertDialog.Builder(this@ResumeUI)
                        dialog.setTitle("실패!")
                        dialog.setMessage(t.message.toString())
                        dialog.show()
                    }

                    // 서버 통신 실패시 구현 부분
                })
            }

            if (insertword == "") {
                val noKeyword = AlertDialog.Builder(this)
                noKeyword.setTitle("키워드 없음")
                    .setMessage("키워드를 입력해주세요.")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                    .setCancelable(false)
                noKeyword.show()
            }

            // 입력받은 단어가 없을 경우에 키워드가 없다는 다이얼로그 창 띄움
        }


        saveBtn.setOnClickListener {
            var insertsubject = subjectCover.text.toString()
            var insertcontent = contentsCover.text.toString()

            // 입력한 자기소개서 제목과 내용을 문자열로 변환

            resumeService.saveResume(insertsubject, insertcontent)
                .enqueue(object : Callback<Resume> {
                    override fun onResponse(call: Call<Resume>, response: Response<Resume>) {
                        Toast.makeText(this@ResumeUI, "저장되었습니다", Toast.LENGTH_LONG).show()
                    }

                    // 제목과 내용을 서버로 송신 하면 저장 완료

                    override fun onFailure(call: Call<Resume>, t: Throwable) {
                        var dialog = AlertDialog.Builder(this@ResumeUI)
                        dialog.setTitle("실패!")
                        dialog.setMessage("통신에 실패했습니다")
                        dialog.show()
                    }

                    // 서버 통신 실패시 구현 부분

                })
        }

        downBtn1.setOnClickListener()
        {
            contentsCover.append(recomm1.text.toString())
        }

        downBtn2.setOnClickListener()
        {
            contentsCover.append(recomm2.text.toString())
        }

        downBtn3.setOnClickListener()
        {
            contentsCover.append(recomm3.text.toString())
        }

        downBtn4.setOnClickListener()
        {
            contentsCover.append(recomm4.text.toString())
        }

        downBtn5.setOnClickListener()
        {
            contentsCover.append(recomm5.text.toString())
        }

        // downBtn 버튼은 AI가 생성한 문장을 자기소개서 내용에 넣을 때 사용

        CoverBottomMyPageBtn.setOnClickListener()
        {
            startActivity(MyPageIntent)
        }

        CoverBottomHomeBtn.setOnClickListener()
        {
            startActivity(HomeIntent)
        }

        CoverBottomInterviewBtn.setOnClickListener()
        {
            startActivity(InterviewIntent)
        }

        // 하단 바에 있는 아이콘 클릭시 화면 전환 되는 인텐트 처리
    }

    // 가상키보드 숨기기 구현 코드

    fun hideKeyboard(v: View) {
        if (v != null) {
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}

