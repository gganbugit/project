package com.example.resume2.Register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.resume2.R
import com.example.resume2.login.LoginUI
import kotlinx.android.synthetic.main.registerui.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterUI : AppCompatActivity() {

    var imm: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registerui)

        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        val intent = Intent(this, LoginUI::class.java)

        // 버튼 클릭시 화면전환을 위한 인텐트 변수 선언

        var okHttpClient = OkHttpClient.Builder()
            .build()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://52.78.99.20/login/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        // Retrofit 객체 생성

        var registerService = retrofit.create(RegisterService::class.java)

        // registerService Retrofit 생성

        submitLayout.setOnClickListener() {

            // SIGN UP 버튼을 클릭 했을 때 발생하는 이벤트 처리

            var newid = newuserid.text.toString()
            var newpassword = newuserpassword.text.toString()

            // 이메일, 패스워드를 입력받고 입력받은 값을 문자열로 변환

            registerService.requestRegister(newid, newpassword).enqueue(object: Callback<Register> {
                override fun onResponse(call: Call<Register>, response: Response<Register>) {
                    var Register = response.body()

                    if(newid == Register?.user_id)
                    {
                        startActivity(intent)
                    }
                    
                    // 입력받은 이메일과 패스워드를 서버에 송신하고 LoginUI 로 화면전환
                }
                override fun onFailure(call: Call<Register>, t: Throwable) {
                    var dialog = AlertDialog.Builder(this@RegisterUI)
                    dialog.setTitle("실패!")
                    dialog.setMessage("통신에 실패했습니다")
                    dialog.show()

                    // 서버 통신 실패시 구현 부분
                }
            })
        }
    }

    fun hideKeyboard(v: View) {
        if (v != null) {
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}
