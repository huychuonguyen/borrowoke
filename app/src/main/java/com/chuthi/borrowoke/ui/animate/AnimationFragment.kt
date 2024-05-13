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
import com.chuthi.borrowoke.ext.toggleSlide
import com.chuthi.borrowoke.other.enums.FragmentResultKey
import com.chuthi.borrowoke.other.enums.asString
import com.chuthi.borrowoke.ui.dog.DogFragment
import com.chuthi.borrowoke.util.MyLogManager
import org.koin.android.ext.android.inject
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
                viewModel.setVisibleContent(isVisible)
            }
        }

        handleCallbacks()
    }

    override fun onObserveData(): LifecycleOwner.() -> Unit = {
        viewModel.run {
            getLiveData(contentVisibility) { visible ->
                toggleNews(visible)
            }

            getLiveData(manualText) {
                binding.tvTitle.text = it.asString(context)
            }
        }

    }

    override fun onArgumentsSaved(arguments: Bundle?) {
    }

    private fun handleCallbacks() {
        onChildFragmentResult(
            requestKey = FragmentResultKey.DogFragmentKey()
        ) { key, bundle ->
            // visible news
            key.onVisible(bundle) { isVisible ->
                if (isVisible != viewModel.contentVisibility.value)
                    viewModel.setVisibleContent(isVisible)
            }
        }
    }

    private fun toggleNews(visible: Boolean) {
        binding.run {
            visible.let { visibility ->
                when (visibility) {
                    false -> {
                        glBetween.setPercent(1f)
                        frContents.toggleSlide(View.GONE, onStart = {
                            childFragmentManager.popBackStack()
                        })
                    }

                    else -> {
                        glBetween.setPercent(0.33f)
                        frContents.toggleSlide(View.VISIBLE, onStart = {
                            addDogContents()
                        })
                    }
                }
            }
        }
    }

    private fun addDogContents() {
        val newsFragment = DogFragment.newInstance()
        childFragmentManager.replaceFragment(
            containerId = binding.frContents.id,
            fragment = newsFragment,
            tag = DogFragment.TAG,
            isAddToBackStack = true
        )
    }
}