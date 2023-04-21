package com.chuthi.borrowoke.ui.news

import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.data.model.response.Article
import com.chuthi.borrowoke.data.repo.NewsRepo
import com.chuthi.borrowoke.ext.apiCall
import com.chuthi.borrowoke.ext.launchViewModelScope
import com.chuthi.borrowoke.other.enums.CommonError
import com.chuthi.borrowoke.other.enums.UiText
import com.chuthi.borrowoke.woker.MyWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewsViewModel(
    myWorker: MyWorker,
    private val newsRepo: NewsRepo
) : BaseViewModel() {

    private val _breakingNews = MutableStateFlow<List<Article>>(emptyList())
    val breakingNews = _breakingNews.asStateFlow()

    init {
        // fetch breaking news
        getBreakingNews()
        // run news worker
       // myWorker.runNewsWork()
    }

    /**
     * Get articles of breaking news
     */
    private fun getBreakingNews() = launchViewModelScope {
        showLoading()
        newsRepo.getBreakingNews().apiCall(
            onSuccess = { news ->
                val articles = news.data?.articles ?: emptyList()
                _breakingNews.emit(articles)
            },
            onError = {
                val code = it.errorCode
                val message = it.message ?: ""
                commonError.emit(
                    CommonError.NormalError(
                        message = UiText.DynamicString("$code: $message"),
                        code = code
                    )
                )
            },
            onFinished = {
                hideLoading()
            }
        )
    }
}