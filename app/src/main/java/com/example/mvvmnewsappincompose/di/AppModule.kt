package com.example.mvvmnewsappincompose.di

import android.content.Context
import android.content.SharedPreferences
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

    @Singleton
    @Provides
    fun provideNewsDatabase(@ApplicationContext context: Context): ArticleDatabase {
            return Room.databaseBuilder(
                context,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }

    @Singleton
    @Provides
    fun provideNewsDao(newsDatabase: ArticleDatabase): ArticleDao {
        return newsDatabase.getArticleDao()
    }

    @Singleton
    @Provides
    fun provideNewsRepository(newsDao: ArticleDao): NewsRepository {
        return NewsRepository(newsDao)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class SharedPreferencesModule {

        @Singleton
        @Provides
        fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences("preferences_name", Context.MODE_PRIVATE)
        }
    }

}