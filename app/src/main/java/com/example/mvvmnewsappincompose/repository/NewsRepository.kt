package com.example.mvvmnewsappincompose.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.mvvmnewsappincompose.api.RetrofitInstance
import com.example.mvvmnewsappincompose.db.ArticleDao
import com.example.mvvmnewsappincompose.db.ArticleDatabase
import com.example.mvvmnewsappincompose.models.Article
import com.example.mvvmnewsappincompose.models.NewsResponse
import com.example.mvvmnewsappincompose.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class NewsRepository @Inject constructor(
    val dao: ArticleDao
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) : Resource<NewsResponse> {
        val response = try {
            RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
        } catch (e: Exception){
            return Resource.Error("An unknown error occured!")
        }
        return Resource.Success(response)
    }

    suspend fun upsert(article: Article) = dao.upsert(article)


    /*suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)*/

    //suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews(): LiveData<List<Article>> {
        return dao.getAllArticles()
    }

    suspend fun deleteArticle(article: Article) = dao.deleteArticle(article)
}