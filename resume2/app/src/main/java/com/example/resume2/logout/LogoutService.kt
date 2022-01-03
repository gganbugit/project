package com.example.resume2.logout

import retrofit2.Call
import retrofit2.http.*

interface LogoutService {
    @GET("login/logout/")
    fun getLogout(): Call<Logout>

    @GET("cover/select")
    fun getMypageView(): Call<MyPageView>

    @FormUrlEncoded
    @POST("cover/detail")
    fun getCoverDetail(
        @Field("cover_id") cover_id: Int
    ): Call<MyPageView>

    @FormUrlEncoded
    @POST("cover/update")
    fun getCoverUpdate(
        @Field("cover_id") cover_id : Int,
        @Field("subject") subject : String,
        @Field("content") content : String
    ) : Call<MyPageView>

    @FormUrlEncoded
    @POST("cover/delete")
    fun getCoverDelete(
        @Field("cover_id") cover_id : Int
    ) : Call<MyPageView>
}