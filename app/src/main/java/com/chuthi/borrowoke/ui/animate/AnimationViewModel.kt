package com.chuthi.borrowoke.ui.animate

import androidx.lifecycle.SavedStateHandle
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.ext.launchViewModelScope
import com.chuthi.borrowoke.other.enums.CommonError
import com.chuthi.borrowoke.other.enums.UiText
import kotlinx.coroutines.delay

class AnimationViewModel(private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

    init {
        launchViewModelScope {
            delay(2000)
            commonError.emit(
                CommonError.NormalError(UiText.DynamicString("Má nó"),0)
            )
        }
    }

    val newsVisibility = savedStateHandle.getLiveData(NEWS_VISIBLE_KEY, false)

    fun setVisibleNews(visible: Boolean) = savedStateHandle.set(NEWS_VISIBLE_KEY, visible)

    companion object {
        private const val NEWS_VISIBLE_KEY = "NEWS_VISIBLE_KEY"
    }
}