package com.example.mvvmnewsappincompose.breakingnews

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmnewsappincompose.MainActivity
import com.example.mvvmnewsappincompose.SortType
import com.example.mvvmnewsappincompose.getAllTypes
import com.example.mvvmnewsappincompose.models.Article
import com.example.mvvmnewsappincompose.repository.NewsRepository
import com.example.mvvmnewsappincompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModel @Inject constructor(
    val newsRepository : NewsRepository
) : ViewModel() {

    var isLoading = mutableStateOf(true)
    var loadError = mutableStateOf("")
    var endReached = mutableStateOf(false)
    var currentNews = mutableStateOf<List<Article>>(listOf())
    var breakingNews = mutableStateOf<List<Article>>(listOf())
    var economicNews = mutableStateOf<List<Article>>(listOf())
    var sportsNews = mutableStateOf<List<Article>>(listOf())
    var healthNews = mutableStateOf<List<Article>>(listOf())
    var savedNews = mutableStateOf<List<Article>>(emptyList())
    var searchNews = mutableStateOf<List<Article>>(listOf())
    var isSearching = mutableStateOf(false)
    var currentSnackBarMessage = mutableStateOf("")
    var darkTheme = mutableStateOf(false)

    var breakingNewsPage = 1
    var searchNewsPage = 1

    private var isSearchStarting = true

    var currentSortType = mutableStateOf(getAllTypes()[0])

    init {
        getBreakingNews("us")
        getSavedNews()
        getEconomicNews()
        getSportsNews()
        getHealthNews()
    }

    fun switchDarkMode(){
        darkTheme.value = !darkTheme.value
    }

    fun updateCurrentNews() {
        currentNews.value = when (currentSortType.value) {
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
            //var result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            var result = newsRepository.getBreakingNews("us", breakingNewsPage)
            Log.d("GetBreakingNews Function", "getBreakingNews: ${breakingNewsPage} Breaking news size: ${breakingNews.value.size}")
            when(result) {
                is Resource.Success -> {

                    val breakingNewsArticles = result.data?.articles!!.mapIndexed { index, article ->
                        Article(article.author,article.content,article.description, article.publishedAt, article.source, article.title, article.url, article.urlToImage)
                    }

                    breakingNewsPage++
                    breakingNews.value += breakingNewsArticles
                    if(currentNews.value.isEmpty()){
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
            //var result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            var result = newsRepository.getEconomicNews()
            when(result) {
                is Resource.Success -> {

                    val economicNewsArticles = result.data?.articles!!.mapIndexed { index, article ->
                        Article(article.author,article.content,article.description, article.publishedAt, article.source, article.title, article.url, article.urlToImage)
                    }

                    breakingNewsPage++
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
            //var result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            var result = newsRepository.getSportsNews()
            when(result) {
                is Resource.Success -> {

                    val sportsNewsArticles = result.data?.articles!!.mapIndexed { index, article ->
                        Article(article.author,article.content,article.description, article.publishedAt, article.source, article.title, article.url, article.urlToImage)
                    }

                    breakingNewsPage++
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
            //var result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            var result = newsRepository.getHealthNews()
            when(result) {
                is Resource.Success -> {

                    val healthNewsArticles = result.data?.articles!!.mapIndexed { index, article ->
                        Article(article.author,article.content,article.description, article.publishedAt, article.source, article.title, article.url, article.urlToImage)
                    }

                    breakingNewsPage++
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


    fun searchNews(query: String){

            viewModelScope.launch(Dispatchers.Default) {

                if(query.isEmpty() || query.length < 3) {
                    searchNews.value = emptyList()
                    isSearching.value = false
                    isSearchStarting = true
                    return@launch
                }

                isLoading.value = true
                searchNews.value = emptyList()
                var result = newsRepository.searchNews(query, searchNewsPage)


                when(result) {
                    is Resource.Success -> {
                        endReached.value = searchNewsPage * 20 >= result.data!!.articles.size
                        val searchNewsResults = result.data!!.articles.mapIndexed { index, entry ->
                            Article(entry.author,entry.content,entry.description, entry.publishedAt, entry.source, entry.title, entry.url, entry.urlToImage)
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
                    is Resource.Loading -> {

                    }
                }

            }


    }

    fun saveArticle(article: Article) =
        viewModelScope.launch {
            Log.d("Article Save:", "saveArticle: SAVED ARTICLE HERE")
            newsRepository.upsert(article)
    }

    fun getSavedNews()  =
        newsRepository.getSavedNews().observeForever {
            //savedNews.value = emptyList()
            //currentNews.value = emptyList()
            savedNews.value += it
            //currentNews.value += it
        }

    private fun hasInternetConnection(): Boolean {
        /*val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }*/
        return true

    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.deleteArticle(article)
            _articleDeleted.postValue(true)
        }
    }



    /*private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let {resultResponse ->
                breakingNewsPage++
                if(breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())

    }*/

}

    /*val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null*/

    /*var breakingNews = mutableStateOf(MutableLiveData<Resource<NewsResponse>>())
    var breakingNewsPage = mutableIntStateOf(1)
    var breakingNewsResponse = mutableStateOf(null)

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)

    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let {resultResponse ->
                breakingNewsPage++
                if(breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())

    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let {resultResponse ->
                searchNewsPage++
                if(searchNewsResponse == null){
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())

    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeSearchNewsCall(searchQuery: String){
        searchNews.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable){
            when(t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String){
        breakingNews.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable){
            when(t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        /*val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }*/
        return true

    }*/