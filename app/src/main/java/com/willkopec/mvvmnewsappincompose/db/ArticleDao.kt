package com.willkopec.mvvmnewsappincompose.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.willkopec.mvvmnewsappincompose.models.Article

@Dao
interface ArticleDao{

    @Upsert
    suspend fun upsert(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

}