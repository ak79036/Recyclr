package com.example.wastemangement.Notification

import com.example.wastemangement.Notification.Constants.Companion.Base_url
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitInstance {

companion object{
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(Base_url).addConverterFactory(GsonConverterFactory.create()).build()
    }

    val api by lazy {
        retrofit.create(NotificationApi::class.java)
    }
}

}