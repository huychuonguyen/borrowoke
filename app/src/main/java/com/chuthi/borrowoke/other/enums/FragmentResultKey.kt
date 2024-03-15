package com.chuthi.borrowoke.other.enums

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.chuthi.borrowoke.data.model.response.Article
import com.chuthi.borrowoke.ext.getData
import com.chuthi.borrowoke.ext.setFragmentResultData

/**************************************
- Created by Chuong Nguyen
- Email : huychuonguyen@gmail.com
- Date : 08/05/2024
- Project : Base Kotlin
 **************************************/

/**
 * Set result data for [fragment].
 * @param fragment the fragment to set result data
 * @param resultKey the key to retrieve with [data]
 * @param data the result data
 */
private fun <T> FragmentResultKey.setResultData(
    fragment: Fragment,
    resultKey: String,
    data: T
) = fragment.setFragmentResultData(
    key = this,
    data = arrayOf(Pair(resultKey, data))
)

sealed class FragmentResultKey(val requestKey: String) {

    class StatusAndNavigationBarKey : FragmentResultKey(REQUEST_KEY) {

        fun onResetDefaultColor(bundle: Bundle, callback: (Boolean) -> Unit) {
            val isResetDefault = bundle.getData<Boolean>(RESET_DEFAULT_COLOR_KEY)
            isResetDefault ?: return
            callback.invoke(isResetDefault)
        }

        fun setResetDefaultColor(fragment: Fragment, isReset: Boolean) =
            setResultData(fragment, RESET_DEFAULT_COLOR_KEY, isReset)

        companion object {
            private const val REQUEST_KEY = "STATUS_AND_NAVIGATION_BAR_KEY"
            private const val RESET_DEFAULT_COLOR_KEY = "RESET_DEFAULT_COLOR_KEY"
        }
    }

    class AnimationFragmentKey : FragmentResultKey(REQUEST_KEY) {

        fun onNewsVisible(bundle: Bundle, callback: (Boolean) -> Unit) {
            val isVisible = bundle.getData<Boolean>(VISIBILITY_NEWS_KEY)
            isVisible ?: return
            callback.invoke(isVisible)
        }

        fun onArticleResult(bundle: Bundle, callback: (Article) -> Unit) {
            val article = bundle.getData<Article>(ARTICLE_KEY)
            article ?: return
            callback.invoke(article)
        }

        fun setArticleResult(fragment: Fragment, article: Article) =
            setResultData(fragment, ARTICLE_KEY, article)

        fun setNewsVisibilityResult(fragment: Fragment, visible: Boolean) =
            setResultData(fragment, VISIBILITY_NEWS_KEY, visible)

        companion object {
            private const val REQUEST_KEY = "ANIMATION_FRAGMENT_KEY"
            private const val VISIBILITY_NEWS_KEY = "VISIBILITY_NEWS_KEY"
            private const val ARTICLE_KEY = "ARTICLE_KEY"
        }
    }

    class DogFragmentKey : FragmentResultKey(REQUEST_KEY) {

        fun setVisibilityResult(fragment: Fragment, visible: Boolean) =
            setResultData(fragment, VISIBILITY_DOG_KEY, visible)

        fun onVisible(bundle: Bundle, callback: (Boolean) -> Unit) {
            val isVisible = bundle.getData<Boolean>(VISIBILITY_DOG_KEY)
            isVisible ?: return
            callback.invoke(isVisible)
        }

        companion object {
            private const val REQUEST_KEY = "DOG_FRAGMENT_KEY"
            private const val VISIBILITY_DOG_KEY = "VISIBILITY_DOG_KEY"
        }
    }
}
