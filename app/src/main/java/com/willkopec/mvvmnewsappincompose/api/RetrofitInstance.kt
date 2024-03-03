package com.willkopec.mvvmnewsappincompose.api

import com.willkopec.mvvmnewsappincompose.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*-------------------------------
*
* RetrofitInstance: This file contains the retrofit object which uses
* the baseUrl string to get the api Request
*
* ------------------------------- */

class RetrofitInstance {

    companion object{

        private val retrofit by lazy{

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build()
        }

        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }

    }

}