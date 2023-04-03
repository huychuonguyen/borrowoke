package com.chuthi.borrowoke.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chuthi.borrowoke.base.BaseFragment
import com.chuthi.borrowoke.databinding.FragmentNewsBinding
import com.chuthi.borrowoke.other.adapters.normal.BreakingNewsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFragment : BaseFragment<FragmentNewsBinding, NewsViewModel>() {

    private lateinit var breakingNewsAdapter: BreakingNewsAdapter

    override val viewModel by viewModel<NewsViewModel>()

    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNewsBinding.inflate(inflater, container, false)

    override fun setupUI() {
        binding.run {

        }
        setupRecyclerView()
    }

    override fun onObserveData(): (suspend CoroutineScope.() -> Unit) = {
        viewModel.run {
            launch {
                breakingNews.collectLatest { articles ->
                    breakingNewsAdapter.submitList(articles)
                }
            }
        }
    }

    override fun onArgumentsSaved(arguments: Bundle?) {
    }

    private fun setupRecyclerView() {
        binding.run {
            // init adapter
            breakingNewsAdapter = BreakingNewsAdapter()

            rcvBreakingNews.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = breakingNewsAdapter
            }
        }
    }

    companion object {
        const val TAG = "NewsFragment"

        @JvmStatic
        fun newInstance() = NewsFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}