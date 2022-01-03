package com.example.resume2.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.resume2.IntroUI
import com.example.resume2.MenuUI
import com.example.resume2.R
import com.example.resume2.Resume.ResumeEx
import com.example.resume2.logout.MyPageUI
import com.example.resume2.okhttp_client
import kotlinx.android.synthetic.main.loginui.*
import com.franmontiel.persistentcookiejar.persistence.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import okhttp3.Interceptor.*
import java.net.CookieManager
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


class LoginUI : AppCompatActivity() {

    var imm: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginui)

        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        var MenuIntent = Intent(this, MenuUI::class.java)

        // 버튼 클릭시 화면전환을 위한 인텐트 변수 선언

        var loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        // 서버와 통신 중 로그를 기록하기 위함

        okhttp_client = OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(CookieManager()))
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS).build()
        
        var client = okhttp_client

        // 클라이언트는 젼역변수로 선언한 클라이언트를 사용

        var retrofit = Retrofit.Builder()
            .baseUrl("http://52.78.99.20/login/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Retrofit 선언


        var loginService = retrofit.create(LoginService::class.java)

        // loginService Retrofit 생성


        loginbtnLayout.setOnClickListener {
            var id = userid.text.toString()
            var password = userpassword.text.toString()
            
            // 입력받은 아이디 (이메일) 와 패스워드를 문자열로 변경한다.

            loginService.requestLogin(id, password).enqueue(object : Callback<Login> {
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    val login = response.body()
                    if (id == login?.user_id)
                    {
                        startActivity(MenuIntent)
                    }

                    if (id == "") {
                        Toast.makeText(this@LoginUI, "아이디는 공백이 될 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }

                    if (password == "") {
                        Toast.makeText(this@LoginUI, "비밀번호는 공백이 될 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }

                }
                
                // 입력한 아이디 (이메일), 패스워드를 서버에 전송
                // DB에 아이디 (이메일), 패스워드가 존재하면 로그인 성공 후, 화면 전환

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    var dialog = AlertDialog.Builder(this@LoginUI)
                    dialog.setTitle("실패!")
                    dialog.setMessage("통신에 실패했습니다")
                    dialog.show()
                }
                
                // 서버와 연결이 안되어있을 때, 다이얼로그 창 띄움
            })


        }
    }

    override fun onBackPressed() {
        var IntroIntent = Intent(this, IntroUI::class.java)
        startActivity(IntroIntent)
    }
    
    // 스마트폰에 뒤로가기 버튼을 눌렀을 때, 인트로 화면으로 전환

    fun hideKeyboard(v: View) {
        if (v != null) {
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}




