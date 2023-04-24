package com.chuthi.borrowoke.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.chuthi.borrowoke.base.BaseFragment
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.databinding.FragmentProfileBinding
import com.chuthi.borrowoke.ext.navigateTo
import com.chuthi.borrowoke.ext.onSafeClick
import kotlinx.coroutines.CoroutineScope


class ProfileFragment : BaseFragment<FragmentProfileBinding, BaseViewModel>() {

    override val viewModel: BaseViewModel?
        get() = null

    private val args: ProfileFragmentArgs by navArgs()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun setupUI() {
        binding.run {
            tvTitle.text = getString(args.profileTitle)
            tvTitle.onSafeClick {
                val action = ProfileFragmentDirections.actionGlobalHomeFragment()
                it.navigateTo(action)
            }
        }
    }

    override fun observeFlowData(): (suspend CoroutineScope.() -> Unit)? = null

    override fun onArgumentsSaved(arguments: Bundle?) {
    }

    companion object {
        const val TAG = "ProfileFragment"

        @JvmStatic
        fun newInstance() = ProfileFragment().apply {
            arguments = Bundle().apply {}
        }
    }

}