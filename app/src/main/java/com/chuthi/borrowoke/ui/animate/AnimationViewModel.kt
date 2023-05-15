package com.chuthi.borrowoke.ui.animate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.other.enums.UiText

class AnimationViewModel(private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

    val contentVisibility = savedStateHandle.getLiveData(CONTENT_VISIBLE_KEY, false)

    val manualText = contentVisibility.map {
        if (!it) UiText.StringResource(R.string.tap_sam_to_see_dogs)
        else UiText.StringResource(R.string.tap_sam_to_hide_dogs)
    }

    fun setVisibleContent(visible: Boolean) = savedStateHandle.set(CONTENT_VISIBLE_KEY, visible)

    companion object {
        private const val CONTENT_VISIBLE_KEY = "NEWS_VISIBLE_KEY"
    }
}