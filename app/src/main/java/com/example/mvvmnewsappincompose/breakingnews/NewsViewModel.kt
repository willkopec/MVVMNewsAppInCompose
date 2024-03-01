package com.example.mvvmnewsappincompose.breakingnews

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmnewsappincompose.util.MyPreference
import com.example.mvvmnewsappincompose.util.SortType
import com.example.mvvmnewsappincompose.models.Article
import com.example.mvvmnewsappincompose.repository.NewsRepository
import com.example.mvvmnewsappincompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModel
@Inject
constructor(
    private val newsRepository: NewsRepository,
    private val myPreference: MyPreference,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loadError = MutableStateFlow("")
    val loadError: StateFlow<String> = _loadError

    private val _endReached = MutableStateFlow(false)
    val endReached: StateFlow<Boolean> = _endReached

    private val _breakingNews = mutableStateListOf<List<Article>>(listOf())
    //val breakingNews: StateFlow<List<Article>> = _breakingNews

    var currentNews = mutableStateListOf<List<Article>>(listOf())
    //val currentNews: StateFlow<List<Article>> = _currentNews

    private val _economicNews = mutableStateListOf<List<Article>>(listOf())
    //val economicNews: StateFlow<List<Article>> = _economicNews

    private val _sportsNews = mutableStateListOf<List<Article>>(listOf())
    //val sportsNews: StateFlow<List<Article>> = _sportsNews

    private val _healthNews = mutableStateListOf<List<Article>>(listOf())
    //val healthNews: StateFlow<List<Article>> = _healthNews

    private val _savedNews = MutableStateFlow<List<Article>>(emptyList())
    val savedNews: StateFlow<List<Article>> = _savedNews

    private val _searchNews = MutableStateFlow<List<Article>>(emptyList())
    val searchNews: StateFlow<List<Article>> = _searchNews

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _darkTheme = MutableStateFlow(myPreference.isDarkMode())
    val darkTheme: StateFlow<Boolean> = _darkTheme

    private var breakingNewsPage = 1
    private var searchNewsPage = 1

    private val _currentSortType = MutableStateFlow(SortType.BREAKING)
    val currentSortType: StateFlow<SortType> = _currentSortType

    // Function to update currentSortType
    fun setCurrentSortType(sortType: SortType) {
        _currentSortType.value = sortType
    }

    private val _scrollToTop = MutableLiveData(false)
    val scrollToTop: LiveData<Boolean>
        get() = _scrollToTop

    private val _articleDeleted = MutableLiveData<Boolean>()
    val articleDeleted: LiveData<Boolean> = _articleDeleted

    init {
        getAllNewsLists()
    }

    fun getAllNewsLists() {
        getBreakingNews("us")
        getSavedNews()
        getEconomicNews()
        getSportsNews()
        getHealthNews()

        if (_loadError.value.isNotEmpty()) {
            breakingNewsPage = 1
        }

        _isLoading.value = _loadError.value.isNotEmpty()
        _loadError.value = ""
        updateCurrentNews()
    }

    fun switchDarkMode() {
        val newValue = !_darkTheme.value
        _darkTheme.value = newValue
        viewModelScope.launch { myPreference.switchDarkMode() }
    }

    fun updateCurrentNews() {
        currentNews = when (currentSortType.value) {
            SortType.BREAKING -> _breakingNews
            SortType.ECONOMIC -> _economicNews
            SortType.SPORTS -> _sportsNews
            SortType.HEALTH -> _healthNews
        }
    }

    fun updateScrollToTop(scroll: Boolean) {
        _scrollToTop.postValue(scroll)
    }

    fun updateArticleDeleted(deletion: Boolean) {
        _articleDeleted.postValue(deletion)
    }

    fun getBreakingNews(countryCode: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = newsRepository.getBreakingNews("us", breakingNewsPage)
            when (result) {
                is Resource.Success -> {
                    val breakingNewsArticles = result.data?.articles?.map { article ->
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
                    } ?: emptyList()

                    _loadError.value = ""
                    _isLoading.value = false
                    breakingNewsPage++
                    _breakingNews += breakingNewsArticles
                    if (currentNews.isEmpty()) {
                        currentNews += breakingNewsArticles
                    }
                }
                is Resource.Error -> {
                    _loadError.value = result.message ?: ""
                    _isLoading.value = false
                }
                else -> {}
            }
        }
    }

    fun getEconomicNews() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = newsRepository.getEconomicNews()
            when (result) {
                is Resource.Success -> {
                    val economicNewsArticles = result.data?.articles?.map { article ->
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
                    } ?: emptyList()

                    _economicNews += economicNewsArticles
                }
                is Resource.Error -> {
                    _loadError.value = result.message ?: ""
                    _isLoading.value = false
                }
                else -> {}
            }
        }
    }

    fun getSportsNews() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = newsRepository.getSportsNews()
            when (result) {
                is Resource.Success -> {
                    val sportsNewsArticles = result.data?.articles?.map { article ->
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
                    } ?: emptyList()

                    _sportsNews += sportsNewsArticles
                }
                is Resource.Error -> {
                    _loadError.value = result.message ?: ""
                    _isLoading.value = false
                }
                else -> {}
            }
        }
    }

    fun getHealthNews() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = newsRepository.getHealthNews()
            when (result) {
                is Resource.Success -> {
                    val healthNewsArticles = result.data?.articles?.map { article ->
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
                    } ?: emptyList()

                    _healthNews += healthNewsArticles
                }
                is Resource.Error -> {
                    _loadError.value = result.message ?: ""
                    _isLoading.value = false
                }
                else -> {}
            }
        }
    }

    fun searchNews(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty() || query.length < 3) {
                _searchNews.value = emptyList()
                _isSearching.value = false
                return@launch
            }

            _isLoading.value = true
            _searchNews.value = emptyList()
            val result = newsRepository.searchNews(query, searchNewsPage)

            when (result) {
                is Resource.Success -> {
                    _isSearching.value = true
                    _endReached.value = searchNewsPage * 20 >= result.data?.articles?.size ?: 0
                    val searchNewsResults = result.data?.articles?.map { entry ->
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
                    } ?: emptyList()
                    searchNewsPage++

                    _loadError.value = ""
                    _isLoading.value = false
                    _searchNews.value += searchNewsResults
                }
                is Resource.Error -> {
                    _loadError.value = result.message ?: ""
                    _isLoading.value = false
                }
                else -> {}
            }
        }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.upsert(article)
        }
    }

    fun getSavedNews() {
        newsRepository.getSavedNews().observeForever {
            _savedNews.value = it
        }
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.deleteArticle(article)
            _articleDeleted.postValue(true)
        }
    }

}