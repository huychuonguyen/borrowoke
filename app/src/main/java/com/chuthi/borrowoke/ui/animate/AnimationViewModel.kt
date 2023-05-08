package com.chuthi.borrowoke.ui.animate

import androidx.lifecycle.SavedStateHandle
import com.chuthi.borrowoke.base.BaseViewModel

class AnimationViewModel(private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

    val newsVisibility = savedStateHandle.getLiveData(NEWS_VISIBLE_KEY, false)

    fun setVisibleNews(visible: Boolean) = savedStateHandle.set(NEWS_VISIBLE_KEY, visible)

    companion object {
        private const val NEWS_VISIBLE_KEY = "NEWS_VISIBLE_KEY"
    }
}