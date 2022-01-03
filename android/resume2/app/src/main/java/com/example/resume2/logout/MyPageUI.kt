package com.example.resume2.logout


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.resume2.MenuUI
import com.example.resume2.MyPage.MyCoverUI
import com.example.resume2.R
import com.example.resume2.Resume.ResumeEx
import com.example.resume2.TTS.*
import com.example.resume2.login.LoginUI
import com.example.resume2.okhttp_client
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.mypagefragui.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class MyPageUI : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypagefragui)

        var LogintIntent = Intent(this, LoginUI::class.java)
        var MyCoverIntent = Intent(this, MyCoverUI::class.java)
        var MyInterviewIntent = Intent(this, MyInterviewUI::class.java)
        var CoverIntent = Intent(this, ResumeEx::class.java)
        var InterviewIntent = Intent(this, TTEx::class.java)
        var HomeIntent = Intent(this, MenuUI::class.java)

        // 버튼 클릭시 화면전환을 위한 인텐트 변수 선언

        var gson = GsonBuilder().setLenient().create()

        var loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        // 서버와 통신 중 로그를 기록하기 위함

        var client = okhttp_client

        // 클라이언트는 젼역변수로 선언한 클라이언트를 사용

        var retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://52.78.99.20/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        // Retrofit 객체 생성

        var logoutService = retrofit.create(LogoutService::class.java)
        var ttsService = retrofit.create(ttsService::class.java)

        // logoutService, ttsService Retrofit 생성

        fun setAdapter(coverList: ArrayList<cover>) {
            val mAdapter = RecyclerAdapter(coverList, this@MyPageUI)
            CoverFrag.adapter = mAdapter
            CoverFrag.layoutManager = LinearLayoutManager(this@MyPageUI)
            CoverFrag.setHasFixedSize(false)

            // 저장된 자기소개서를 뿌려주는 함수 선언

            mAdapter.listener = object : OnCoverItemClickListener { // 저장된 자기소개서를 클릭했을 때, 발생하는 이벤트 처리
                override fun onCoverClick(holder: RecyclerAdapter.ViewHolder?, view: View?, position: Int)
                {
                    val mycoverid = view?.findViewById<TextView>(R.id.myCoverId)
                    mycoverid?.text

                    Toast.makeText(this@MyPageUI, "수정페이지로 이동했습니다.", Toast.LENGTH_SHORT).show()

                    var mycover_id = mycoverid?.text.toString()

                    // cover_id 로 서버와 통신을 하기 위해 cover_id 를 문자로 변경

                    logoutService.getCoverDetail(mycover_id.toInt()).enqueue(object : Callback<MyPageView> {
                            override fun onResponse(call: Call<MyPageView>, response: Response<MyPageView>) {
                                val result = response.body()
                                result.let {
                                    it?.cover?.get(0)?.let { it1 ->
                                        MyCoverIntent.putExtra("coverSubject", it1.subject)
                                    }

                                    it?.cover?.get(0)?.let { it2 ->
                                        MyCoverIntent.putExtra("coverContent", it2.content)
                                    }

                                    MyCoverIntent.putExtra("coverCoverId", mycover_id)

                                    /* cover_id 를 서버로 송신하면 서버에서는 자기소개서 제목과 내용, cover_id 를 송신하고
                                        putExtra 를 활용하여 서버에서 송신한 제목, 내용, cover_id 를 다음 화면으로 넘겨준다 */
                                    startActivity(MyCoverIntent)
                                }
                            }

                            override fun onFailure(call: Call<MyPageView>, t: Throwable) {

                            // 서버 통신 실패시 구현 부분
                            }
                        })
                }
            }
        }


        fun setAdapter2(interviewList: ArrayList<TTSMember>) {
            val mAdapter2 = RecyclerAdapter2(interviewList, this@MyPageUI)
            InterviewFrag.adapter = mAdapter2
            InterviewFrag.layoutManager = LinearLayoutManager(this@MyPageUI)
            InterviewFrag.setHasFixedSize(false)

            // 저장된 모의면접을 뿌려주는 함수 선언

            mAdapter2.listener2 = object : OnInterviewItemClickListener {

                override fun onInterviewClick(holder: RecyclerAdapter2.ViewHolder, view: View?, position: Int)
                {
                    val myInterviewid = view?.findViewById<TextView>(R.id.myInterviewId)
                    myInterviewid?.text

                    Toast.makeText(this@MyPageUI, "수정페이지로 이동했습니다.", Toast.LENGTH_SHORT).show()

                    var myInterview_id = myInterviewid?.text.toString()

                    // interview_id 로 서버와 통신을 하기 위해 interview_id 를 문자로 변경

                    ttsService.getInterviewDetail(myInterview_id.toInt())
                        .enqueue(object : Callback<MyPageView> {
                            override fun onResponse(call: Call<MyPageView>, response: Response<MyPageView>) {
                                val result = response.body()
                                result?.let {
                                    it?.interview?.get(0)?.let { it1 ->
                                        MyInterviewIntent.putExtra("interviewQuestion", it1.question
                                        )
                                    }

                                    it?.interview?.get(0)?.let { it2 ->
                                        MyInterviewIntent.putExtra("interviewAnswer", it2.answer)
                                    }

                                    MyInterviewIntent.putExtra("interviewId", myInterview_id)

                                    /* interview_id 를 서버로 송신하면 서버에서는 자기소개서 제목과 내용, interview_id 를 송신하고
                                        putExtra 를 활용하여 서버에서 송신한 질문, 답변, interview_id 를 다음 화면으로 넘겨준다 */

                                    startActivity(MyInterviewIntent)
                                }
                            }

                            override fun onFailure(call: Call<MyPageView>, t: Throwable) {

                            // 서버 통신 실패시 구현 부분
                            }
                        })
                }
            }
        }


        logoutService.getMypageView().enqueue(object : Callback<MyPageView> {
            override fun onResponse(call: Call<MyPageView>, response: Response<MyPageView>) {
                var pageview = response.body()
                
                // 마이페이지로 이동했을 때, 처음에 보여지는 화면에 대한 Retrofit 정의
                
                pageview?.let {
                    if (it.covers[0].cover_count == 0) {
                        CoverCounter.text = it.covers[0].cover_count.toString()
                        InterviewCounter.text = it.covers[0].interview_count.toString()
                        loginUserid.text = it.covers[0].user_id
                        
                        // 저장된 자기소개서가 없을 때, 빈 리스트를 뿌려주기 위함.
                    
                    } else if (it.covers[0].cover_count != 0) {
                        setAdapter(it.covers)
                        CoverCounter.text = it.covers[0].cover_count.toString()
                        InterviewCounter.text = it.covers[0].interview_count.toString()
                        loginUserid.text = it.covers[0].user_id

                        // 저장된 자기소개서가 1개 이상일 때, 저장된 리스트를 뿌려주기 위함.
                    }
                }
            }

            override fun onFailure(call: Call<MyPageView>, t: Throwable) {

            // 서버 통신 실패시 구현 부분
            }

        })
        CoverListBtn1.setOnClickListener {
            CoverFrag.visibility = (View.VISIBLE)
            InterviewFrag.visibility = (View.INVISIBLE)

            /* 자기소개서 리스트 버튼을 클릭 했을 때, 발생하는 이벤트 처리
                저장된 자기소개서 리스트는 보여지고, 저장된 모의면접 리스트는 보여지지 않는다. */
            logoutService.getMypageView().enqueue(object : Callback<MyPageView> {
                override fun onResponse(call: Call<MyPageView>, response: Response<MyPageView>) {
                    var pageview = response.body()
                    pageview?.let {

                        if (it.covers[0].cover_count == 0) {
                            CoverCounter.text = it.covers[0].cover_count.toString()
                            InterviewCounter.text = it.covers[0].interview_count.toString()
                            loginUserid.text = it.covers[0].user_id

                            // 저장된 자기소개서가 없을 때, 빈 리스트를 뿌려주기 위함.

                        } else if (it.covers[0].cover_count != 0) {
                            setAdapter(it.covers)
                            CoverCounter.text = it.covers[0].cover_count.toString()
                            InterviewCounter.text = it.covers[0].interview_count.toString()
                            loginUserid.text = it.covers[0].user_id

                            // 저장된 자기소개서가 1개 이상일 때, 저장된 리스트를 뿌려주기 위함.
                        }
                    }
                }

                override fun onFailure(call: Call<MyPageView>, t: Throwable) {
                // 서버 통신 실패시 구현 부분
                }

            })
        }

        InterviewListBtn1.setOnClickListener {
            CoverFrag.visibility = (View.INVISIBLE)
            InterviewFrag.visibility = (View.VISIBLE)

            /* 모의면접 리스트 버튼을 클릭 했을 때, 발생하는 이벤트 처리
                저장된 모의면접 리스트는 보여지고, 저장된 자기소개서 리스트는 보여지지 않는다. */
            ttsService.getMyInterview().enqueue(object : Callback<MyPageView> {
                override fun onResponse(call: Call<MyPageView>, response: Response<MyPageView>) {
                    var interPageview = response.body()
                    interPageview?.let {

                        if (it.interviews[0].interview_count == 0) {
                            CoverCounter.text = it.interviews[0].cover_count.toString()
                            InterviewCounter.text = it.interviews[0].interview_count.toString()
                            loginUserid.text = it.interviews[0].user_id

                            // 저장된 모의면접이 없을 때, 빈 리스트를 뿌려주기 위함.
                        } else if (it.interviews[0].interview_count != 0) {
                            setAdapter2(it.interviews)
                            CoverCounter.text = it.interviews[0].cover_count.toString()
                            InterviewCounter.text = it.interviews[0].interview_count.toString()
                            loginUserid.text = it.interviews[0].user_id

                            // 저장된 모의면접이 1개 이상일 때, 저장된 리스트를 뿌려주기 위함.
                        }
                    }
                }

                override fun onFailure(call: Call<MyPageView>, t: Throwable) {
                // 서버 통신 실패시 구현 부분
                }

            })


        }

        logoutBtn.setOnClickListener()
        {
            // 로그아웃 버튼을 클릭했을 때 발생하는 이벤트처리
            val logoutCheck = AlertDialog.Builder(this)
            logoutCheck.setMessage("로그아웃 하시겠습니까?")
                .setNegativeButton("로그아웃", DialogInterface.OnClickListener { dialog, id ->
                    logoutService.getLogout().enqueue(object : Callback<Logout> {
                        override fun onResponse(call: Call<Logout>, response: Response<Logout>) {
                            Toast.makeText(this@MyPageUI, "로그아웃 성공", Toast.LENGTH_LONG).show()
                            startActivity(LogintIntent)
                        }

                        override fun onFailure(call: Call<Logout>, t: Throwable) {
                        }
                    })
                })
                .setPositiveButton("취소", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
        })
            .setCancelable(false)
            logoutCheck.show()
        }
        // 로그아웃 버튼을 클릭하면 로그아웃 결정 여부를 묻는 다이얼로그 창을 띄움

        MyPageBottomMenuBtn.setOnClickListener {
            startActivity(HomeIntent)
        }

        MyPageBottomCoverBtn.setOnClickListener {
            startActivity(CoverIntent)
        }

        MyPageBottomInterviewBtn.setOnClickListener {
            startActivity(InterviewIntent)
        }

        // 하단 바에 있는 아이콘 클릭시 화면 전환 되는 인텐트 처리
    }
}