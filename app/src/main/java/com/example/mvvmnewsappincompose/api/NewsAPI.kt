package com.example.mvvmnewsappincompose.api

import com.example.mvvmnewsappincompose.models.NewsResponse
import com.example.mvvmnewsappincompose.util.Constants.Companion.API_KEY
import com.example.mvvmnewsappincompose.util.Constants.Companion.API_KEY2
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber :  Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY2
    ): NewsResponse

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber :  Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY2
    ): NewsResponse

    @GET("v2/everything")
    suspend fun getEconomicNews(
        @Query("q")
        searchQuery: String = "economy nasdaq federal-reserve stocks",
        @Query("page")
        pageNumber :  Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY2
    ): NewsResponse

}