package com.chuthi.borrowoke.ui.news

import android.os.Bundle
import com.chuthi.borrowoke.other.enums.FragmentResultKey

class ChildNewsFragment : NewsFragment() {

    override fun handleFragmentBackPressed(): (() -> Unit) = {
        val animationFragmentKey = FragmentResultKey.AnimationFragmentKey()
        animationFragmentKey.setNewsVisibilityResult(this, false)
    }

    companion object {
        const val TAG = "ChildNewsFragment"

        @JvmStatic
        fun newInstance() = ChildNewsFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}