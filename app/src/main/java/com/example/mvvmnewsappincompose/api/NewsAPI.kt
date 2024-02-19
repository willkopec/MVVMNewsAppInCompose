package com.example.mvvmnewsappincompose.api

import com.example.mvvmnewsappincompose.models.NewsResponse
import com.example.mvvmnewsappincompose.util.Constants.Companion.API_KEY
import com.example.mvvmnewsappincompose.util.Constants.Companion.API_KEY2
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/* -------------------
*
* NewsAPI: This file contains all of the GET request Queries from the NewsAPI
*
* -------------------*/

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber :  Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): NewsResponse

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber :  Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): NewsResponse

    @GET("v2/everything")
    suspend fun getEconomicNews(
        @Query("q")
        searchQuery: String = "economy nasdaq federal-reserve stocks",
        @Query("page")
        pageNumber :  Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): NewsResponse

    @GET("v2/everything")
    suspend fun getSportsNews(
        @Query("q")
        searchQuery: String = "sports football soccer tennis basketball",
        @Query("page")
        pageNumber :  Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): NewsResponse

    @GET("v2/everything")
    suspend fun getHealthNews(
        @Query("q")
        searchQuery: String = "health fitness gym sick",
        @Query("page")
        pageNumber :  Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): NewsResponse

}