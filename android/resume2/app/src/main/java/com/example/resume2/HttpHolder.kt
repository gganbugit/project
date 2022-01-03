package com.example.resume2

import okhttp3.OkHttpClient

var okhttp_client: OkHttpClient? = null
/* HttpClient 를 전역변수로 사용.
   로그인 로그아웃 기능에서 세션유지를 위함. */