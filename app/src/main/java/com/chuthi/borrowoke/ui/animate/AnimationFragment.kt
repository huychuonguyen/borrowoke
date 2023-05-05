package com.chuthi.borrowoke.ui.animate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.BaseFragment
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.databinding.FragmentAnimationBinding
import com.chuthi.borrowoke.ext.onSafeClick
import com.chuthi.borrowoke.ext.setPercent
import com.chuthi.borrowoke.ext.toggleSlideUpDown
import com.chuthi.borrowoke.ui.news.NewsFragment

/**
 * The fragment to test contents animation. Yolo ^^
 */
class AnimationFragment : BaseFragment<FragmentAnimationBinding, BaseViewModel>() {
    override val viewModel: BaseViewModel?
        get() = null

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentAnimationBinding.inflate(inflater, container, false)

    override fun setupUI() {
        binding.run {
            lnTitle.onSafeClick {
                toggleNews()
            }
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

    fun toggleNews() {
        binding.run {
            frContents.visibility.let { visibility ->
                when (visibility) {
                    View.VISIBLE -> {
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
        childFragmentManager.beginTransaction()
            .replace(R.id.frContents, newsFragment, NewsFragment.TAG)
            .addToBackStack(NewsFragment.TAG)
            .commit()
    }
}