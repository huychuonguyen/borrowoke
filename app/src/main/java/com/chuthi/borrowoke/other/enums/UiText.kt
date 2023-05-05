package com.chuthi.borrowoke.other.enums

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {

    data class DynamicString(val value: String) : UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    object Empty : UiText()
}

fun UiText.asString(context: Context?): String? {
    context ?: return null
    return when (this) {
        is UiText.DynamicString -> value
        is UiText.StringResource -> context.getString(resId, *args)
        is UiText.Empty -> ""
    }
}