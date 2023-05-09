package com.chuthi.borrowoke.ui.animate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.chuthi.borrowoke.base.BaseFragment
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.databinding.FragmentAnimationBinding
import com.chuthi.borrowoke.ext.getLiveData
import com.chuthi.borrowoke.ext.onChildFragmentResult
import com.chuthi.borrowoke.ext.onSafeClick
import com.chuthi.borrowoke.ext.replaceFragment
import com.chuthi.borrowoke.ext.setPercent
import com.chuthi.borrowoke.ext.showToast
import com.chuthi.borrowoke.ext.toggleSlideUpDown
import com.chuthi.borrowoke.other.enums.FragmentResultKey
import com.chuthi.borrowoke.ui.news.NewsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * The fragment to test contents animation. Yolo ^^
 */
class AnimationFragment : BaseFragment<FragmentAnimationBinding, BaseViewModel>() {

    override val viewModel by viewModel<AnimationViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentAnimationBinding.inflate(inflater, container, false)

    override fun setupUI() {
        binding.run {
            lnTitle.onSafeClick {
                val isVisible = frContents.visibility != View.VISIBLE
                viewModel.setVisibleNews(isVisible)
            }
        }

        handleCallbacks()
    }

    override fun observeLiveData(): (LifecycleOwner.() -> Unit) = {
        getLiveData(viewModel.newsVisibility) { visible ->
            toggleNews(visible)
        }
    }

    override fun onArgumentsSaved(arguments: Bundle?) {
    }

    override fun handleFragmentBackPressed() {
        val alreadyNews = childFragmentManager.findFragmentByTag(NewsFragment.TAG)
        if (alreadyNews == null) super.handleFragmentBackPressed()
        else {
            childFragmentManager.popBackStack()
        }
    }

    private fun handleCallbacks() {
        onChildFragmentResult(
            requestKey = FragmentResultKey.AnimationFragmentKey()
        ) { key, bundle ->
            // visible news
            key.onNewsVisible(bundle) { isVisible ->
                if (isVisible != viewModel.newsVisibility.value)
                    viewModel.setVisibleNews(isVisible)
            }
            // article
            key.onArticleResult(bundle) { article ->
                // hide news
                viewModel.setVisibleNews(false)
                val description = article.description ?: "No-desc"
                showToast(description)
            }
        }
    }

    private fun toggleNews(visible: Boolean) {
        binding.run {
             visible.let { visibility ->
                 when (visibility) {
                     false -> {
                         glBetween.setPercent(1f)
                         frContents.toggleSlideUpDown(View.GONE, onStart = {
                             childFragmentManager.popBackStack()
                         })
                     }

                     else -> {
                         glBetween.setPercent(0.33f)
                         frContents.toggleSlideUpDown(View.VISIBLE, onStart = {
                             addNewsContents()
                         })
                     }
                 }
             }
         }
    }

    private fun addNewsContents() {
        val newsFragment = NewsFragment.newInstance()
        childFragmentManager.replaceFragment(
            containerId = binding.frContents.id,
            fragment = newsFragment,
            tag = NewsFragment.TAG,
            isAddToBackStack = true
        )
    }
}