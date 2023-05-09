package com.chuthi.borrowoke.ui.news

import androidx.lifecycle.asLiveData
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.data.model.response.Article
import com.chuthi.borrowoke.data.repo.NewsRepo
import com.chuthi.borrowoke.ext.apiCall
import com.chuthi.borrowoke.ext.launchViewModelScope
import com.chuthi.borrowoke.other.enums.CommonError
import com.chuthi.borrowoke.other.enums.UiText
import kotlinx.coroutines.flow.MutableStateFlow

class NewsViewModel(
    private val newsRepo: NewsRepo
) : BaseViewModel() {

    private val _breakingNews = MutableStateFlow<List<Article>>(emptyList())
    val breakingNews = _breakingNews.asLiveData()

    init {
        getBreakingNews()
    }
    /**
     * Get articles of breaking news
     */
    fun getBreakingNews() = launchViewModelScope {
        showLoading()
        newsRepo.getBreakingNews().apiCall(
            { news ->
                val articles = news.data?.articles ?: emptyList()
                _breakingNews.emit(articles)
            }, {
                val code = it.errorCode
                val message = it.message ?: ""
                commonError.emit(
                    CommonError.NormalError(
                        message = UiText.DynamicString("$code: $message"),
                        code = code
                    )
                )
            }, {
                hideLoading()
            }
        )
    }

    /**
     * Clear all news
     */
    fun clearBreakingNews() = launchViewModelScope {
        _breakingNews.emit(emptyList())
    }
}