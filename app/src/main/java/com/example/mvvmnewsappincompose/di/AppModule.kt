package com.example.mvvmnewsappincompose.di

import android.content.Context
import androidx.room.Room
import com.example.mvvmnewsappincompose.api.NewsAPI
import com.example.mvvmnewsappincompose.db.ArticleDao
import com.example.mvvmnewsappincompose.db.ArticleDatabase
import com.example.mvvmnewsappincompose.repository.NewsRepository
import com.example.mvvmnewsappincompose.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    //@Singleton
    @Provides
    fun provideNewsDatabase(@ApplicationContext context: Context): ArticleDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }

    @Provides
    fun provideNewsDao(newsDatabase: ArticleDatabase): ArticleDao {
        return newsDatabase.getArticleDao()
    }

}