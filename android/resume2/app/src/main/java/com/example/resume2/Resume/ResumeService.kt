package com.example.resume2.Resume

import com.example.resume2.Resume.Resume
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ResumeService {
    @FormUrlEncoded
    @POST("cv_service/")
    fun requestResume(
        @Field("input1") input1:String
    ) : Call<Resume>

    @FormUrlEncoded
    @POST("cover/create")
    fun saveResume(
        @Field("subject") subject : String,
        @Field("content") content : String
    ) : Call<Resume>
}