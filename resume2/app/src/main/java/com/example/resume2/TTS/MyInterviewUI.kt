package com.example.resume2.TTS

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
import com.example.resume2.logout.MyPageUI
import com.example.resume2.okhttp_client
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.interviewdetailui.*
import kotlinx.android.synthetic.main.myinterviewui.*
import kotlinx.android.synthetic.main.mypagefragui.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyInterviewUI : AppCompatActivity() {
    var imm: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.interviewdetailui)

        var myIQ = intent.getStringExtra("interviewQuestion")
        var myIA = intent.getStringExtra("interviewAnswer")
        var myII = intent.getStringExtra("interviewId")

        myinterviewdetailQuestion.setText(myIQ).toString()
        myinterviewdetailAnswer.setText(myIA).toString()
        myinterviewdetailInterviewId.setText(myII).toString()

        var MyPageIntent = Intent(this, MyPageUI::class.java)
        var HomeIntent = Intent(this, MenuUI::class.java)
        var CoverIntent = Intent(this, ResumeEx::class.java)
        var InterviewIntent = Intent(this, TTEx::class.java)

        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        // 가상키보드 숨기기 구현 코드

        var loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        var gson = GsonBuilder().setLenient().create()

        var client = okhttp_client

        var retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://52.78.99.20/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        var ttsService = retrofit.create(ttsService::class.java)

        var interviewId_TextView: TextView? = findViewById(R.id.myinterviewdetailInterviewId)
        var interviewQuestion_EditText: EditText? = findViewById(R.id.myinterviewdetailQuestion)
        var interviewAnswer_EditText: EditText? = findViewById(R.id.myinterviewdetailAnswer)

        var myinterviewid = interviewId_TextView?.text.toString()
        var updateQuestion = interviewQuestion_EditText?.text
        var updateAnswer = interviewAnswer_EditText?.text

        InterviewChangeDeleteBtn.setOnClickListener() {
            var interview_popup = PopupMenu(this@MyInterviewUI, it)
            menuInflater.inflate(R.menu.interviewchangedeleteui, interview_popup.menu)
            interview_popup.show()
            interview_popup.setOnMenuItemClickListener() {
                when (it.itemId) {
                    R.id.InterviewChangeBtn -> {
                        ttsService.getInterviewUpdate(myinterviewid.toInt(), updateQuestion.toString(), updateAnswer.toString()).enqueue(object :
                            Callback<TTS>
                        {
                            override fun onResponse(call: Call<TTS>, response: Response<TTS>)
                            {
                                MyPageIntent.putExtra("interviewQuestion", updateQuestion.toString())
                                MyPageIntent.putExtra("interviewAnswer", updateAnswer.toString())
                                MyPageIntent.putExtra("interviewId", myinterviewid)

                                var myUII = MyPageIntent.getStringExtra("interviewId")
                                var myUIQ = MyPageIntent.getStringExtra("interviewQuestion")
                                var myUIA = MyPageIntent.getStringExtra("interviewAnswer")

                                myInterviewQuestion?.setText(myUIQ).toString()
                                myInterviewId?.setText(myUII).toString()
                                myInterviewAnswer?.setText(myUIA).toString()

                                Toast.makeText(this@MyInterviewUI, "수정 완료", Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<TTS>, t: Throwable) {

                            }
                        })

                        return@setOnMenuItemClickListener true
                    }

                    R.id.InterviewDeleteBtn -> {
                        val deleteYesNo = AlertDialog.Builder(this)
                        deleteYesNo.setTitle("삭제")
                            .setMessage("삭제 하시겠습니까?")
                            .setNegativeButton("삭제", DialogInterface.OnClickListener { dialog, id ->
                                ttsService.getInterviewDelete(myinterviewid.toInt()).enqueue(object :
                                    Callback<TTS> {
                                    override fun onResponse(call: Call<TTS>, response: Response<TTS>) {
                                        Toast.makeText(this@MyInterviewUI, "삭제 완료", Toast.LENGTH_SHORT).show()
                                        startActivity(MyPageIntent)
                                    }

                                    override fun onFailure(call: Call<TTS>, t: Throwable) {

                                    }
                                })
                            })
                            .setPositiveButton("취소", DialogInterface.OnClickListener { dialog, id ->
                                dialog.dismiss()
                            })
                            .setCancelable(false)
                        deleteYesNo.show()
                        return@setOnMenuItemClickListener true
                    }

                    R.id.InterviewShareBtn -> {
                        try {
                            val sendText = "질문 : $updateQuestion\n답변 : $updateAnswer"
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
                    else -> {
                        return@setOnMenuItemClickListener true
                    }
                }
            }
        }

        InterviewDetailHomeBtn.setOnClickListener {
            startActivity(HomeIntent)
        }

        InterviewDetailCoverBtn.setOnClickListener {
            startActivity(CoverIntent)
        }

        InterviewDetailInterviewBtn.setOnClickListener {
            startActivity(InterviewIntent)
        }

        InterviewDetailMyPageBtn.setOnClickListener {
            startActivity(MyPageIntent)
        }
    }



    fun hideKeyboard(v: View) {
        if (v != null) {
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}