package com.willkopec.mvvmnewsappincompose.repository

import androidx.lifecycle.LiveData
import com.willkopec.mvvmnewsappincompose.api.RetrofitInstance
import com.willkopec.mvvmnewsappincompose.db.ArticleDao
import com.willkopec.mvvmnewsappincompose.models.Article
import com.willkopec.mvvmnewsappincompose.models.NewsResponse
import com.willkopec.mvvmnewsappincompose.util.Resource
import javax.inject.Inject

class NewsRepository @Inject constructor(
    val dao: ArticleDao
) {

    /********************************************
    * Room Local Database data/information functions
    *
    * These functions are used to store, delete and retrieve data from the article Database
    *
    ********************************************/
    suspend fun upsert(article: Article) = dao.upsert(article)

    suspend fun deleteArticle(article: Article) = dao.deleteArticle(article)

    fun getSavedNews(): LiveData<List<Article>> {
        return dao.getAllArticles()
    }

    /********************************************
     * Retrofit API request functions from NewsApi.org
     *
     * These functions are used to retrieve data from the API via retrofit instance requests
     *
     ********************************************/

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) : Resource<NewsResponse> {
        val response = try {
            RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
        } catch (e: Exception){
            return Resource.Error("An unknown error occured!")
        }
        return Resource.Success(response)
    }

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Resource<NewsResponse> {
        val response = try {
            RetrofitInstance.api.searchForNews(searchQuery)
        } catch (e: Exception){
            return Resource.Error("An unknown error occured!")
        }
        return Resource.Success(response)
    }


    suspend fun getEconomicNews(): Resource<NewsResponse>{
        val response = try {
            RetrofitInstance.api.getEconomicNews()
        } catch (e: Exception){
            return Resource.Error("An unknown error occured!")
        }
        return Resource.Success(response)
    }

    suspend fun getSportsNews(): Resource<NewsResponse>{
        val response = try {
            RetrofitInstance.api.getSportsNews()
        } catch (e: Exception){
            return Resource.Error("An unknown error occured!")
        }
        return Resource.Success(response)
    }

    suspend fun getHealthNews(): Resource<NewsResponse>{
        val response = try {
            RetrofitInstance.api.getHealthNews()
        } catch (e: Exception){
            return Resource.Error("An unknown error occured!")
        }
        return Resource.Success(response)
    }

}