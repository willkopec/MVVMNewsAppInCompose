package com.willkopec.mvvmnewsappincompose.api

import com.willkopec.mvvmnewsappincompose.models.NewsResponse
import com.willkopec.mvvmnewsappincompose.util.Constants.Companion.API_KEY
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
        searchQuery: String = "health fitness medicine",
        @Query("page")
        pageNumber :  Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): NewsResponse

}