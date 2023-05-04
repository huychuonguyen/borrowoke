package com.chuthi.borrowoke.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.BaseFragment
import com.chuthi.borrowoke.databinding.FragmentNewsBinding
import com.chuthi.borrowoke.ext.getFlowDataLasted
import com.chuthi.borrowoke.ext.getLiveData
import com.chuthi.borrowoke.ext.onSafeClick
import com.chuthi.borrowoke.ext.showSnackBar
import com.chuthi.borrowoke.ext.showToast
import com.chuthi.borrowoke.other.adapters.normal.BreakingNewsAdapter
import com.chuthi.borrowoke.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFragment : BaseFragment<FragmentNewsBinding, NewsViewModel>() {

    private lateinit var breakingNewsAdapter: BreakingNewsAdapter

    private val newsArgs: NewsFragmentArgs by navArgs()

    override val viewModel by viewModel<NewsViewModel>()

    private val mainViewModel: MainViewModel by activityViewModels()

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
    }

    override fun observeLiveData(): (LifecycleOwner.() -> Unit) = {
        viewModel.run {
            getLiveData(breakingNews) {
                breakingNewsAdapter.submitList(it)
            }
        }
    }

    override fun observeFlowData(): CoroutineScope.() -> Unit = {
        getFlowDataLasted(mainViewModel.sharedData) {
            showToast("Shared data: $it")
        }
    }

    override fun onArgumentsSaved(arguments: Bundle?) {
    }

    private fun setupRecyclerView() {
        binding.run {
            // init adapter
            breakingNewsAdapter = BreakingNewsAdapter { binding, item ->
                binding.root.showSnackBar(
                    message = item.author,
                    actionText = context?.getString(R.string.open)
                ) {
                    /*  item.author ?: return@showSnackBar
                      showToast(item.author)*/
                    findNavController().popBackStack()
                }
            }

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