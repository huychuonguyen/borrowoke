package com.chuthi.borrowoke.ui.dog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.chuthi.borrowoke.base.SwipeRefreshFragment
import com.chuthi.borrowoke.databinding.FragmentDogBinding
import com.chuthi.borrowoke.ext.getFlowDataLasted
import com.chuthi.borrowoke.ext.gone
import com.chuthi.borrowoke.ext.openBrowser
import com.chuthi.borrowoke.ext.show
import com.chuthi.borrowoke.ext.showSnackBar
import com.chuthi.borrowoke.other.adapters.normal.DogAdapter
import com.chuthi.borrowoke.other.enums.FragmentResultKey
import org.koin.androidx.viewmodel.ext.android.viewModel

class DogFragment : SwipeRefreshFragment<FragmentDogBinding, DogViewModel>() {

    override val viewModel by viewModel<DogViewModel>()

    private lateinit var dogAdapter: DogAdapter

    override fun swipeRefreshLayout() = binding.swipeRefresh

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDogBinding.inflate(inflater, container, false)

    override fun setupUI() {
        setupRecyclerView()
    }

    override fun onArgumentsSaved(arguments: Bundle?) {
    }

    override fun onObserveData(): (LifecycleOwner.() -> Unit) = {
        getFlowDataLasted(viewModel.dog) {
            dogAdapter.submitList(it)
        }
    }

    override fun showLoading() {
        showRefreshing()
        binding.tvWaitASec.show()
    }

    override fun hideLoading() {
        hideRefreshing()
        binding.tvWaitASec.gone()
    }

    override fun onRefresh() {
        viewModel.clearDogs()
        viewModel.getDogs()
        super.onRefresh()
    }

    override fun handleFragmentBackPressed(): (() -> Unit) = {
        val dogFragmentKey = FragmentResultKey.DogFragmentKey()
        dogFragmentKey.setVisibilityResult(this, false)
    }

    private fun setupRecyclerView() {
        dogAdapter = DogAdapter { imageUrl ->
            binding.root.showSnackBar(message = "Open image", actionText = "open") {
                (context as? AppCompatActivity)?.openBrowser(url = imageUrl)
            }
        }
        binding.run {
            rcvDogs.apply {
                layoutManager = GridLayoutManager(context, 3)
                adapter = dogAdapter
            }
        }
    }

    companion object {
        const val TAG = "DogFragment"

        @JvmStatic
        fun newInstance() = DogFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}