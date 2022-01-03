package com.example.resume2.TTS

import com.example.resume2.logout.MyPageView
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ttsService {
    @FormUrlEncoded
    @POST("interview/create")
    fun saveInterview(
        @Field("question") question : String,
        @Field("answer") answer : String
    ) : Call<TTS>

    @GET("interview/select")
    fun getMyInterview(): Call<MyPageView>

    @FormUrlEncoded
    @POST("interview/detail")
    fun getInterviewDetail(
        @Field("interview_id") interview_id : Int
    ): Call<MyPageView>

    @FormUrlEncoded
    @POST("interview/update")
    fun getInterviewUpdate(
        @Field("interview_id") interview_id : Int,
        @Field("question") question : String,
        @Field("answer") answer : String
    ) : Call<TTS>

    @FormUrlEncoded
    @POST("interview/delete")
    fun getInterviewDelete(
        @Field("interview_id") interview_id : Int
    ) : Call<TTS>

}