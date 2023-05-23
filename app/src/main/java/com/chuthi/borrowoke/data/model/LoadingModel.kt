package com.chuthi.borrowoke.data.model

import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.BaseModel
import com.chuthi.borrowoke.other.enums.UiText


data class LoadingModel(
    val loadingState: LoadingState = LoadingState.None,
    val content: UiText = UiText.StringResource(resId = R.string.loading),
) : BaseModel() {
    sealed interface LoadingState {
        object Loading : LoadingState
        object None : LoadingState
        object Error : LoadingState
    }
}
