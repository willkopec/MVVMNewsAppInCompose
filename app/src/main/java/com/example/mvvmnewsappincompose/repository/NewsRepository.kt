package com.example.mvvmnewsappincompose.repository

import com.example.mvvmnewsappincompose.api.RetrofitInstance
import com.example.mvvmnewsappincompose.db.ArticleDatabase
import com.example.mvvmnewsappincompose.models.Article
import com.example.mvvmnewsappincompose.models.NewsResponse
import com.example.mvvmnewsappincompose.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class NewsRepository @Inject constructor(
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) : Resource<NewsResponse> {
        val response = try {
            RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
        } catch (e: Exception){
            return Resource.Error("An unknown error occured!")
        }
        return Resource.Success(response)
    }


    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    //suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    //fun getSavedNews() = db.getArticleDao().getAllArticles()

    //suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}