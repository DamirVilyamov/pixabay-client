package com.example.pixabayclient.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {
    @GET("api/")
    fun getPosts(
        @Query("key") key:String,
        @Query("q") searchQuery: String,
        @Query("image_type") imageType:String
    ): Call<Post?>
}