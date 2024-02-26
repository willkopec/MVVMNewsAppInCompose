package com.example.mvvmnewsappincompose.breakingnews

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmnewsappincompose.MyPreference
import com.example.mvvmnewsappincompose.SortType
import com.example.mvvmnewsappincompose.models.Article
import com.example.mvvmnewsappincompose.repository.NewsRepository
import com.example.mvvmnewsappincompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Collections.addAll
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModel
@Inject
constructor(
    val newsRepository: NewsRepository,
    val myPreference: MyPreference,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var isLoading = mutableStateOf(true)
    var loadError = mutableStateOf("")
    var endReached = mutableStateOf(false)
    var currentNews = mutableStateOf<List<Article>>(listOf())
    var breakingNews = mutableStateOf<List<Article>>(listOf())
    var economicNews = mutableStateOf<List<Article>>(listOf())
    var sportsNews = mutableStateOf<List<Article>>(listOf())
    var healthNews = mutableStateOf<List<Article>>(listOf())
    var savedNews = mutableStateListOf<Article>()
    var searchNews = mutableStateOf<List<Article>>(listOf())
    var isSearching = mutableStateOf(false)
    var darkTheme = mutableStateOf(myPreference.isDarkMode())

    var breakingNewsPage = 1
    var searchNewsPage = 1

    private var isSearchStarting = true

    var currentSortType = mutableStateOf(SortType.BREAKING)

    init {
        getAllNewsLists()
    }

    fun getAllNewsLists() {
        getBreakingNews("us")
        getSavedNews()
        getEconomicNews()
        getSportsNews()
        getHealthNews()

        if (loadError.value.isNotEmpty()) {
            breakingNewsPage = 1
        }

        Log.d(
            "GetAllNews Function",
            "LoadError : ${loadError.value} Breaking news size: ${breakingNews.value.size}"
        )

        isLoading.value = loadError.value.isNotEmpty()
        loadError.value = ""
        updateCurrentNews()
    }

    fun switchDarkMode() {
        val newValue = !darkTheme.value
        darkTheme.value = newValue
        // Save the new value in SharedPreferences
        viewModelScope.launch { myPreference.switchDarkMode() }
    }

    fun updateCurrentNews() {
        currentNews.value =
            when (currentSortType.value) {
                SortType.BREAKING -> breakingNews.value
                SortType.ECONOMIC -> economicNews.value
                SortType.SPORTS -> sportsNews.value
                SortType.HEALTH -> healthNews.value
            }
    }

    private var _scrollToTop = MutableLiveData(false)
    val scrollToTop: LiveData<Boolean>
        get() = _scrollToTop

    fun updateScrollToTop(scroll: Boolean) {
        _scrollToTop.postValue(scroll)
    }

    private val _articleDeleted = MutableLiveData<Boolean>()
    val articleDeleted: LiveData<Boolean> = _articleDeleted

    fun updateArticleDeleted(deletion: Boolean) {
        _articleDeleted.postValue(deletion)
    }

    fun getBreakingNews(countryCode: String) {
        viewModelScope.launch {
            isLoading.value = true
            // var result = (PAGE_SIZE, curPage * PAGE_SIZE)
            var result = newsRepository.getBreakingNews("us", breakingNewsPage)
            // Log.d("GetBreakingNews Function", "getBreakingNews: ${breakingNewsPage} Breaking news
            // size: ${breakingNews.value.size}")
            when (result) {
                is Resource.Success -> {

                    val breakingNewsArticles =
                        result.data?.articles!!.mapIndexed { index, article ->
                            Article(
                                article.author,
                                article.content,
                                article.description,
                                article.publishedAt,
                                article.source,
                                article.title,
                                article.url,
                                article.urlToImage
                            )
                        }

                    Log.d(
                        "GetBreakingNews Function",
                        "getBreakingNews: ${breakingNewsPage} Breaking news size: ${result.data?.articles!!.size}"
                    )
                    loadError.value = ""
                    isLoading.value = false
                    breakingNewsPage++
                    breakingNews.value += breakingNewsArticles
                    if (currentNews.value.isEmpty()) {
                        currentNews.value += breakingNewsArticles
                    }
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> {}
            }
        }
    }

    fun getEconomicNews() {
        viewModelScope.launch {
            isLoading.value = true
            // var result = (PAGE_SIZE, curPage * PAGE_SIZE)
            var result = newsRepository.getEconomicNews()
            when (result) {
                is Resource.Success -> {

                    val economicNewsArticles =
                        result.data?.articles!!.mapIndexed { index, article ->
                            Article(
                                article.author,
                                article.content,
                                article.description,
                                article.publishedAt,
                                article.source,
                                article.title,
                                article.url,
                                article.urlToImage
                            )
                        }

                    // loadError.value = ""
                    // isLoading.value = false
                    // breakingNewsPage++
                    economicNews.value += economicNewsArticles
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> {}
            }
        }
    }

    fun getSportsNews() {
        viewModelScope.launch {
            isLoading.value = true
            // var result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            var result = newsRepository.getSportsNews()
            when (result) {
                is Resource.Success -> {

                    val sportsNewsArticles =
                        result.data?.articles!!.mapIndexed { index, article ->
                            Article(
                                article.author,
                                article.content,
                                article.description,
                                article.publishedAt,
                                article.source,
                                article.title,
                                article.url,
                                article.urlToImage
                            )
                        }

                    // breakingNewsPage++
                    // loadError.value = ""
                    // isLoading.value = false
                    sportsNews.value += sportsNewsArticles
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> {}
            }
        }
    }

    fun getHealthNews() {
        viewModelScope.launch {
            isLoading.value = true
            // var result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            var result = newsRepository.getHealthNews()
            when (result) {
                is Resource.Success -> {

                    val healthNewsArticles =
                        result.data?.articles!!.mapIndexed { index, article ->
                            Article(
                                article.author,
                                article.content,
                                article.description,
                                article.publishedAt,
                                article.source,
                                article.title,
                                article.url,
                                article.urlToImage
                            )
                        }

                    // loadError.value = ""
                    // isLoading.value = false
                    // breakingNewsPage++
                    healthNews.value += healthNewsArticles
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> {}
            }
        }
    }

    fun searchNews(query: String) {

        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty() || query.length < 3) {
                searchNews.value = emptyList()
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }

            isLoading.value = true
            searchNews.value = emptyList()
            var result = newsRepository.searchNews(query, searchNewsPage)

            when (result) {
                is Resource.Success -> {
                    isSearching.value = true
                    endReached.value = searchNewsPage * 20 >= result.data!!.articles.size
                    val searchNewsResults =
                        result.data!!.articles.mapIndexed { index, entry ->
                            Article(
                                entry.author,
                                entry.content,
                                entry.description,
                                entry.publishedAt,
                                entry.source,
                                entry.title,
                                entry.url,
                                entry.urlToImage
                            )
                        }
                    searchNewsPage++

                    loadError.value = ""
                    isLoading.value = false
                    searchNews.value += searchNewsResults
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                is Resource.Loading -> {}
                else -> {}
            }
        }
    }

    fun saveArticle(article: Article) =
        viewModelScope.launch {
            Log.d("Article Save:", "saveArticle: SAVED ARTICLE HERE")
            newsRepository.upsert(article)
        }

    fun getSavedNews() =
        newsRepository.getSavedNews().observeForever {
            // currentNews.value = emptyList()
            savedNews.clear()
            savedNews.apply { addAll(it) }
            // currentNews.value += it
        }

    private fun hasInternetConnection(): Boolean {
        /*val connectivityManager = getApplication()//getApplication.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager*/
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return true
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.deleteArticle(article)
            _articleDeleted.postValue(true)
        }
    }
}
