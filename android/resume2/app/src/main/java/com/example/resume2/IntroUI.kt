package com.example.resume2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.example.resume2.Register.RegisterUI
import com.example.resume2.login.LoginUI
import kotlinx.android.synthetic.main.introui.*

class IntroUI : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.introui)
        val imageView = findViewById<ImageView>(R.id.intro)
        Glide.with(this)
            .asGif()
            .load(R.raw.newintro)
            .listener(object : RequestListener<GifDrawable?> {
                override fun onLoadFailed(e: GlideException?,
                    model: Any,
                    target: Target<GifDrawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any,
                    target: Target<GifDrawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.setLoopCount(1)
                    return false
                }
            }).into(imageView)

        btnLogin.setOnClickListener()
        {
            val intent = Intent(this, LoginUI::class.java)
            startActivity(intent)
        }


        btnSignUp.setOnClickListener()
        {
            val intent = Intent(this, RegisterUI::class.java)
            startActivity(intent)
        }

    }
}