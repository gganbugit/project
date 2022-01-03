package com.example.resume2.MyPage

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.resume2.MenuUI
import com.example.resume2.R
import com.example.resume2.Resume.ResumeEx
import com.example.resume2.Resume.ResumeUI
import com.example.resume2.TTS.TTEx
import com.example.resume2.TTS.TTSUI
import com.example.resume2.logout.LogoutService
import com.example.resume2.logout.MyPageUI
import com.example.resume2.logout.MyPageView
import com.example.resume2.okhttp_client
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.coverdetailui.*
import kotlinx.android.synthetic.main.interviewdetailui.*
import kotlinx.android.synthetic.main.mycoverui.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyCoverUI : AppCompatActivity() {
    var imm: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coverdetailui)

        var myCS = intent.getStringExtra("coverSubject")
        var myCC = intent.getStringExtra("coverContent")
        var myCI = intent.getStringExtra("coverCoverId")

        // MyPageUI 에서 putExtra 로 넘겨준 값을 받는다.

        var coverIdTextView: TextView? = findViewById(R.id.mycoverdetailCoverId)
        var coverSubjectEditText: EditText? = findViewById(R.id.mycoverdetailSubject)
        var coverContentEditText: EditText? = findViewById(R.id.mycoverdetailContent)

        // findViewById 로 TextView, EditText를 선언한다.

        coverSubjectEditText?.setText(myCS).toString()
        coverContentEditText?.setText(myCC).toString()
        coverIdTextView?.setText(myCI).toString()

        mycoverdetailSubject?.setText(myCS).toString()
        mycoverdetailContent?.setText(myCC).toString()
        mycoverdetailCoverId?.setText(myCI).toString()

        // MyPageUI 에서 putExtra 로 넘겨준 값을 findViewById 로 선언한 TextView, EditText 에 값을 넣어준다.

        var MyPageIntent = Intent(this, MyPageUI::class.java)
        var HomeIntent = Intent(this, MenuUI::class.java)
        var CoverIntent = Intent(this, ResumeEx::class.java)
        var InterviewIntent = Intent(this, TTEx::class.java)

        // 버튼 클릭시 화면전환을 위한 인텐트 변수 선언

        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?

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

        var logoutService = retrofit.create(LogoutService::class.java)

        // logoutService Retrofit 생성


        var mycoverid = coverIdTextView?.text.toString()
        var updateSubject = coverSubjectEditText?.text
        var updateContent = coverContentEditText?.text

        /* cover_id, subject (자기소개서 제목), content (자기소개서 내용) 으로
           서버와 통신 하기 위해 cover_id는 문자열로 변경 */

        ChangeDeleteBtn.setOnClickListener() {
            // 팝업 메뉴 버튼 클릭시 발생하는 이벤트 처리

            var popup = PopupMenu(this@MyCoverUI, it)

            // 팝업 메뉴 객체 생성

            menuInflater.inflate(R.menu.changedeleteui, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener() {
                when (it.itemId) {
                    R.id.ChangeBtn -> {
                        logoutService.getCoverUpdate(mycoverid.toInt(), updateSubject.toString(), updateContent.toString()).enqueue(object :
                            Callback<MyPageView>
                        {
                            override fun onResponse(call: Call<MyPageView>, response: Response<MyPageView>)
                            {
                                MyPageIntent.putExtra("coverSubject", updateSubject.toString())
                                MyPageIntent.putExtra("coverContent", updateContent.toString())
                                MyPageIntent.putExtra("coverCoverId", mycoverid)

                                var myUCI = MyPageIntent.getStringExtra("coverCoverId")
                                var myUCS = MyPageIntent.getStringExtra("coverSubject")
                                var myUCC = MyPageIntent.getStringExtra("coverContent")

                                myCoverSubject?.setText(myUCS).toString()
                                myCoverCon?.setText(myUCC).toString()
                                myCoverId?.setText(myUCI).toString()

                                Toast.makeText(this@MyCoverUI, "수정 완료", Toast.LENGTH_SHORT).show()
                            }

                            /* 팝업 메뉴 중 저장 (수정) 버튼을 눌렀을 때 발생하는 이벤트 처리
                               수정한 자기소개서 제목, 내용 및 cover_id 를 서버로 송신
                               putExtra 를 활용하여 서버에서 송신한 제목, 내용, cover_id 를 다음 화면으로 넘겨준다 */
                            override fun onFailure(call: Call<MyPageView>, t: Throwable)
                            {

                            }
                            // 서버 통신 실패시 구현 부분
                        })

                        return@setOnMenuItemClickListener true
                    }
                    R.id.DeleteBtn -> {
                        val deleteYesNo = AlertDialog.Builder(this)
                        deleteYesNo.setTitle("삭제")
                            .setMessage("삭제 하시겠습니까?")
                            .setNegativeButton("삭제", DialogInterface.OnClickListener { dialog, id ->
                                logoutService.getCoverDelete(mycoverid.toInt()).enqueue(object : Callback<MyPageView>
                                {
                                    override fun onResponse(call: Call<MyPageView>, response: Response<MyPageView>)
                                    {
                                        Toast.makeText(this@MyCoverUI, "삭제 완료", Toast.LENGTH_SHORT).show()
                                        startActivity(MyPageIntent)
                                    }

                                    /* 팝업 메뉴 중 삭제 버튼을 눌렀을 때 발생하는 이벤트 처리
                                       삭제 버튼을 눌렀을 때, 다이얼로그 창이 뜨며 삭제 여부를 확인 */
                                    override fun onFailure(call: Call<MyPageView>, t: Throwable)
                                    {

                                    }
                                    // 서버 통신 실패시 구현 부분
                                })
                            })
                            .setPositiveButton("취소", DialogInterface.OnClickListener{ dialog, id ->
                                dialog.dismiss()
                                // 다이얼로그 창에 취소 버튼을 누르면 다이얼로그 창을 종료
                            })
                            .setCancelable(false)
                        deleteYesNo.show()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.ShareBtn -> {
                        try {
                            val sendText = "제목 : $updateSubject\n내용 : $updateContent"
                            val sendIntent = Intent()
                            sendIntent.action = Intent.ACTION_SEND
                            sendIntent.putExtra(Intent.EXTRA_TEXT, sendText)
                            sendIntent.type = "text/plain"
                            startActivity(Intent.createChooser(sendIntent, "공유하기"))
                        } catch (ignored: ActivityNotFoundException) {
                            Log.d("test", "ignored : $ignored")
                        }
                        return@setOnMenuItemClickListener true
                    }
                    /* 팝업 메뉴 중 공유하기 버튼을 눌렀을 때 발생하는 이벤트 처리
                       스마트폰에 설치되어 있는 어플리케이션 중 공유기능이 있는 어플리케이션 목록들을 보여줌 */
                    else -> {
                        return@setOnMenuItemClickListener true
                    }
                }
            }
        }

        CoverDetailHomeBtn.setOnClickListener {
            startActivity(HomeIntent)
        }

        CoverDetailCoverBtn.setOnClickListener {
            startActivity(CoverIntent)
        }

        CoverDetailInterviewBtn.setOnClickListener {
            startActivity(InterviewIntent)
        }

        CoverDetailMyPageBtn.setOnClickListener {
            startActivity(MyPageIntent)
        }

        // 하단 바에 있는 아이콘 클릭시 화면 전환 되는 인텐트 처리
    }
    fun hideKeyboard(v: View) {
        if (v != null) {
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}
