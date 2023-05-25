package com.chuthi.borrowoke.other.adapters.normal

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.ConcatAdapter
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.data.model.LoadingModel
import com.chuthi.borrowoke.data.model.MessageModel
import com.chuthi.borrowoke.other.enums.UiText

class ChatAdapter(
    onMessageClicked: (MessageModel) -> Unit,
    onItemLongClicked: ((MessageModel) -> Unit)?,
    onLoadingClicked: () -> Unit
) {
    private val messageAdapter = MessageAdapter(onMessageClicked, onItemLongClicked)

    private val loadingAdapter = LoadingAdapter(onLoadingClicked)

    val instance = ConcatAdapter(messageAdapter, loadingAdapter)

    fun submitList(messages: List<MessageModel>) = messageAdapter.submitList(messages)

    fun setLoading(isLoading: Boolean = false) {
        val loadingModels = if (isLoading) listOf(
            LoadingModel(
                loadingState = LoadingModel.LoadingState.Loading,
                content = UiText.StringResource(R.string.wait_for_assistant)
            )
        ) else listOf(
            LoadingModel(
                loadingState = LoadingModel.LoadingState.None,
                content = UiText.StringResource(R.string.wait_for_assistant)
            )
        )

        loadingAdapter.submitList(loadingModels)
    }

    fun setError(error: String?, @DrawableRes src: Int? = null) {
        error ?: return
        val loadingModels = listOf(
            LoadingModel(
                loadingState = LoadingModel.LoadingState.Error,
                content = UiText.DynamicString(error),
                icSrc = src
            )
        )
        loadingAdapter.submitList(loadingModels)
    }

    fun setResetting(mess: String?) {
        mess ?: return
        val loadingModels = listOf(
            LoadingModel(
                loadingState = LoadingModel.LoadingState.Loading,
                content = UiText.DynamicString(mess)
            )
        )
        loadingAdapter.submitList(loadingModels)
    }
}