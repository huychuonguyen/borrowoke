package com.chuthi.borrowoke.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chuthi.borrowoke.base.BaseFragment
import com.chuthi.borrowoke.databinding.FragmentHomeBinding
import com.chuthi.borrowoke.ext.showToast
import com.chuthi.borrowoke.other.OUTPUT_BLUR_WORKER
import com.chuthi.borrowoke.other.adapters.normal.UserAdapter
import com.chuthi.borrowoke.other.adapters.paging.UserPagingAdapter
import com.chuthi.borrowoke.woker.BlurWorker.Companion.PROGRESS
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    private lateinit var userAdapter: UserAdapter

    private lateinit var userPagingAdapter: UserPagingAdapter

    override val viewModel: HomeViewModel by viewModel()

    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onArgumentsSaved(arguments: Bundle?) {
        val title = arguments?.getString(TITLE_ARG) ?: ""
        viewModel.saveStateTitle(title)
    }

    override fun setupUI() {
        binding.run {
            btnIncreaseFirst.setOnClickListener {
                viewModel.increaseFirstCounter()
            }

            btnIncreaseSecond.setOnClickListener {
                viewModel.increaseSecondCounter()
            }

            tvHomeTitle.setOnClickListener {
                showToast("count: ${viewModel.counter.value}")
            }
        }
        setupRecyclerView()
    }

    override fun onObserveData(): (suspend CoroutineScope.() -> Unit) = {
        viewModel.run {
            launch {
                counter.collectLatest {
                    tvCounter.text = it.toString()
                }
            }
            launch {
                countState.collectLatest {
                    tvHomeTitle.text = it
                }
            }
            launch {
                userState.collectLatest {
                    userAdapter.submitList(it)
                }
            }
            launch {
                userPaging.collectLatest {
                    userPagingAdapter.submitData(it)
                }
            }
            // worker
            launch {
                blurImageWorkInfo.collectLatest { workInfo ->
                    workInfo ?: return@collectLatest

                    val progressValue = workInfo.progress.getInt(PROGRESS, -1)
                    if (progressValue != -1) showToast("progress: $progressValue")

                    val outputData = workInfo.outputData
                    val blurOutput =
                        outputData.getString(OUTPUT_BLUR_WORKER) ?: return@collectLatest
                    Log.i("blur_worker_output", blurOutput)
                    showToast(blurOutput)
                }
            }
        }
    }

    override fun onDestroyView() {
        viewModel.saveStateTitle("Medal không có chơi đồ")
        super.onDestroyView()
    }

    private fun setupRecyclerView() {
        binding.apply {
            // init user adapter
            userAdapter = UserAdapter(
                onItemClick = { user, _ ->
                    viewModel.removeUser(user)
                },
                onItemLongClick = { user, _ ->
                    val newUser = user.copy(
                        name = "Medal Gumi"
                    )
                    viewModel.updateUser(newUser)
                })

            userPagingAdapter = UserPagingAdapter(
                onItemLongClick = { user, position ->
                    val newUser = user.copy(
                        name = "Medal nè"
                    )
                    viewModel.updateUserPaging(newUser)
                },
                onItemClick = { user, position ->
                    viewModel.removeUserPaging(user)
                })

            rcvUser.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = userPagingAdapter
            }
        }
    }


    companion object {
        const val TAG = "HomeFragment"
        private const val TITLE_ARG = "title"

        @JvmStatic
        fun newInstance(title: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE_ARG, title)
                }
            }
    }
}