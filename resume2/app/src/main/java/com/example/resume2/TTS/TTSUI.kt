package com.example.resume2.TTS

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.resume2.MenuUI
import com.example.resume2.R
import com.example.resume2.Resume.ResumeEx
import com.example.resume2.logout.MyPageUI
import com.example.resume2.okhttp_client
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.ttsui.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class TTSUI : AppCompatActivity()
{
    var imm : InputMethodManager? = null
    var ttsObj : TextToSpeech? = null
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognitionListener: RecognitionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ttsui)
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?

        var MyPageIntent = Intent(this, MyPageUI::class.java)
        var HomeIntent = Intent(this, MenuUI::class.java)
        var CoverIntent = Intent(this, ResumeEx::class.java)


        requestPermission()

        var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")

        setListener()

        answerBtn.setOnClickListener {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            speechRecognizer.setRecognitionListener(recognitionListener)
            speechRecognizer.startListening(intent)
        }

        ttsObj = TextToSpeech(this, TextToSpeech.OnInitListener {
            if(it!= TextToSpeech.ERROR)
            {
                ttsObj?.language = Locale.KOREAN
            }
        })

        var interview_list = listOf("자기소개를 1분동안 해주세요.", "우리 회사에 지원한 지원동기를 설명해주세요.","성격의 장단점을 설명해주세요.","입사 후 구체적으로 어떤 업무를 하게 될 것이라 생각하십니까?",
            "해당 직무와 관련한 자신의 강점이 무엇이라 생각하십니까?","학창시절 동아리나 사회활동 경험에 대해서 말해 보세요.","이력서에 작성한 경력사항 중 가장 기억에 남는 일을 말씀해주세요.",
            "살면서 무언가 성취했던 경험에 대해 말씀해주세요.","실패한 경험과 그 경험을 통해 배운것을 말씀해주세요.","스트레스 관리 방법에 대해 말씀해 주세요.",
            "입사 후 포부를 말씀해주세요.","지원자가 희망하는 연봉은 얼마인가요?","지원자의 집과 회사가 거리가 먼데 어떻게 다니실 계획인가요?","만약 상사가 부당한 지시를 내린다면 어떻게 하시겠습니까?",
            "본인이 원하는 부서에 배치가 되지 않으면 어떻게 하겠습니까?","우리 회사에 대해 아는대로 말씀해주세요.","동료와 의견충돌이 생긴다면 어떻게 풀어나가겠습니까?",
            "인생관이나 좌우명이 있다면 그 이유와 함께 말씀해 주세요.","10년 후 본인의 모습을 구체적으로 그려보세요.","마지막 할말과 하고 싶은 질문이 있다면 말씀해주세요.")

        interview_list.iterator()


        queryBtn.setOnClickListener {
            val rangeInterger = ThreadLocalRandom.current().nextInt(0,20)
            val utteranceId = this.hashCode().toString() + "0"
            ttsObj?.setPitch(0.9f)
            ttsObj?.setSpeechRate(0.85f)
            ttsObj?.speak(interview_list[rangeInterger],TextToSpeech.QUEUE_FLUSH,null, utteranceId)

            queryText.text=interview_list[rangeInterger]

            Log.d("voice", "voice")

        }

        var gson = GsonBuilder().setLenient().create()

        var loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        var client = okhttp_client

        var retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://52.78.99.20/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        var ttsService = retrofit.create(ttsService::class.java)

        saveTTSBtn.setOnClickListener()
        {
            var queryTextView = queryText.text.toString()
            var answerTextView = answerText.text.toString()

            ttsService.saveInterview(queryTextView, answerTextView).enqueue(object : Callback<TTS> {
                override fun onResponse(call: Call<TTS>, response: Response<TTS>) {
                    Toast.makeText(this@TTSUI, "저장되었습니다", Toast.LENGTH_LONG).show()

                }
                override fun onFailure(call: Call<TTS>, t: Throwable) {
                    var dialog = AlertDialog.Builder(this@TTSUI)
                    dialog.setTitle("실패!")
                    dialog.setMessage(t.message.toString())
                    dialog.show()
                }
            })
        }

        TTSBottomMenuBtn.setOnClickListener {
            startActivity(HomeIntent)
        }

        TTSBottomCoverBtn.setOnClickListener {
            startActivity(CoverIntent)
        }

        TTSBottomMyPageBtn.setOnClickListener {
            startActivity(MyPageIntent)
        }



    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //TextToSpeech객체를 중지하고 삭제함
        if(ttsObj!=null){
            ttsObj?.stop()
            ttsObj?.shutdown()
            ttsObj = null
        }

    }

    fun hideKeyboard(v: View) {
        if(v != null){
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    private fun setListener() {
        recognitionListener = object: RecognitionListener {

            override fun onReadyForSpeech(params: Bundle?) {
                Toast.makeText(applicationContext, "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onBeginningOfSpeech() { }

            override fun onRmsChanged(rmsdB: Float) { }

            override fun onBufferReceived(buffer: ByteArray?) { }

            override fun onEndOfSpeech() { }

            override fun onError(error: Int) {
                var message: String
                when (error)
                {
                    SpeechRecognizer.ERROR_AUDIO -> message = "오디오 에러"
                    SpeechRecognizer.ERROR_CLIENT -> message = "클라이언트 에러"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> message = "퍼미션 없음"
                    SpeechRecognizer.ERROR_NETWORK -> message = "네트워크 에러"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> message = "네트워크 타임아웃"
                    SpeechRecognizer.ERROR_NO_MATCH -> message = "찾을 수 없음"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> message = "RECOGNIZER가 바쁨"
                    SpeechRecognizer.ERROR_SERVER -> message = "서버가 이상함"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> message = "말하는 시간초과"
                    else -> message = "알 수 없는 오류"
                }
                Toast.makeText(applicationContext, "에러 발생 $message", Toast.LENGTH_SHORT).show()
            }
            override fun onResults(results: Bundle?)
            {
                var matches: ArrayList<String> = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<String>

                for (i in 0 until matches.size) {
                    answerText.text = matches[i]
                    answerText.setMovementMethod(ScrollingMovementMethod())

                }
            }
            override fun onPartialResults(partialResults: Bundle?) { }
            override fun onEvent(eventType: Int, params: Bundle?) { }
        }
    }
}