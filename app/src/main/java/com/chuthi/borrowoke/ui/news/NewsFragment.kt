package com.chuthi.borrowoke.ui.news

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.SwipeRefreshFragment
import com.chuthi.borrowoke.databinding.FragmentNewsBinding
import com.chuthi.borrowoke.ext.getFlowDataLasted
import com.chuthi.borrowoke.ext.getLiveData
import com.chuthi.borrowoke.ext.onSafeClick
import com.chuthi.borrowoke.ext.showSnackBar
import com.chuthi.borrowoke.other.adapters.normal.BreakingNewsAdapter
import com.chuthi.borrowoke.other.enums.FragmentResultKey
import com.chuthi.borrowoke.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class NewsFragment : SwipeRefreshFragment<FragmentNewsBinding, NewsViewModel>() {

    private lateinit var breakingNewsAdapter: BreakingNewsAdapter

    private val newsArgs: NewsFragmentArgs by navArgs()

    override val viewModel by viewModel<NewsViewModel>()

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun getViewModels() = listOf(mainViewModel, viewModel)

    override fun swipeRefreshLayout() = binding.swipeRefresh

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNewsBinding.inflate(inflater, container, false)

    override fun setupUI() {
        binding.run {
            tvNewsTitle.text = newsArgs.title

            tvNewsTitle.onSafeClick {
                /*val action = NewsFragmentDirections.actionNewsFragmentToProfileFragment(
                    R.string.borrowoke
                )
                it.navigateTo(action)*/

                val counter = mainViewModel.sharedData.value + " Nè"
                mainViewModel.setSharedData(counter)
            }
        }
        setupRecyclerView()
        // transparent status/ navigation bar
        setStatusAndNavigationColor(Color.RED)
    }

    override fun onWindowInsets(rootView: View, top: Int, bottom: Int) {
        rootView.apply {
            setPadding(
                paddingLeft,
                paddingTop + top,
                paddingRight,
                paddingBottom + bottom
            )
        }
    }

    override fun onObserveData(): LifecycleOwner.() -> Unit = {
        getFlowDataLasted(mainViewModel.sharedData) {
            Log.i("Shared data:", it)
        }

        getLiveData(viewModel.breakingNews) {
            breakingNewsAdapter.submitList(it)
        }
    }

    override fun onArgumentsSaved(arguments: Bundle?) {
    }

    override fun handleFragmentBackPressed(): (() -> Unit) = {
        findNavController().popBackStack()
        resetDefaultStatusAndNavigationColor()
    }

    private fun setupRecyclerView() {
        binding.run {
            // init adapter
            breakingNewsAdapter = BreakingNewsAdapter { binding, item ->
                binding.root.showSnackBar(
                    message = item.author,
                    actionText = context?.getString(R.string.open)
                ) {
                    val animationFragmentKey = FragmentResultKey.AnimationFragmentKey()
                    animationFragmentKey.setArticleResult(this@NewsFragment, item)
                }
            }

            rcvBreakingNews.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = breakingNewsAdapter
            }
        }
    }

    override fun showLoading() {
        // binding.progressBar.visibility = View.VISIBLE
        showRefreshing()
    }

    override fun hideLoading() {
        //binding.progressBar.visibility = View.GONE
        hideRefreshing()
    }

    override fun onRefresh() {
        super.onRefresh()
        // clear all current
        viewModel.clearBreakingNews()
        // get new list
        viewModel.getBreakingNews(pageNumber = 2)
    }

    companion object {
        const val TAG = "NewsFragment"

        @JvmStatic
        fun newInstance() = NewsFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}