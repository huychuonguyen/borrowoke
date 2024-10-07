package com.chuthi.borrowoke.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chuthi.borrowoke.base.BaseFragment
import com.chuthi.borrowoke.databinding.FragmentHomeBinding
import com.chuthi.borrowoke.ext.getFlowDataLasted
import com.chuthi.borrowoke.ext.getLiveData
import com.chuthi.borrowoke.ext.navigateTo
import com.chuthi.borrowoke.ext.onParentFragmentResult
import com.chuthi.borrowoke.ext.onSafeClick
import com.chuthi.borrowoke.ext.showToast
import com.chuthi.borrowoke.other.NO_DELAY_CLICK_INTERVAL
import com.chuthi.borrowoke.other.adapters.normal.UserAdapter
import com.chuthi.borrowoke.other.adapters.paging.UserPagingAdapter
import com.chuthi.borrowoke.other.enums.FragmentResultKey
import com.chuthi.borrowoke.ui.main.MainViewModel
import com.chuthi.borrowoke.util.MyLifecycleOwner
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    private lateinit var userAdapter: UserAdapter

    private lateinit var userPagingAdapter: UserPagingAdapter

    private val mainViewModel: MainViewModel by activityViewModels()

    override val viewModel: HomeViewModel by viewModel()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onArgumentsSaved(arguments: Bundle?) {
        val title = arguments?.getString(TITLE_ARG) ?: ""
        viewModel.saveStateTitle(title)
    }

    override fun setupUI() {
        binding.run {
            btnIncreaseFirst.onSafeClick(NO_DELAY_CLICK_INTERVAL) {
                viewModel.increaseFirstCounter()
            }

            btnIncreaseSecond.onSafeClick(NO_DELAY_CLICK_INTERVAL) {
                viewModel.increaseSecondCounter()
            }

            tvHomeTitle.onSafeClick { v ->
                val action =
                    HomeFragmentDirections.actionHomeFragmentToNewsFragment()
                v.navigateTo(action)
            }

            tvCounter.onSafeClick {
                /* val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment(
                     profileTitle = R.string.home_to_profile
                 )
                 v.navigateTo(action)*/
                mainViewModel.setSharedData(tvCounter.text.toString())
            }

            tvAnimation.onSafeClick {
                val action = HomeFragmentDirections.actionHomeFragmentToChatFragment()
                it.navigateTo(action)
            }
        }
        setupRecyclerView()

        handleCallbacks()
    }

    private fun handleCallbacks() {
        val requestKey = FragmentResultKey.AnimationFragmentKey()
        onParentFragmentResult(requestKey) { key, bundle ->
            key.onArticleResult(bundle) {
                showToast("From News BackPressed")
            }
        }
    }

    override fun onObserveData(): (MyLifecycleOwner.() -> Unit) = {
        viewModel.run {

            binding.run {
                getFlowDataLasted(counter) {
                    tvCounter.text = it.toString()
                }

                getFlowDataLasted(countState) {
                    tvHomeTitle.text = it
                }

                getFlowDataLasted(allUserModel) {
                    userAdapter.submitList(it)
                }
                // worker
                /*launch {
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
                }*/
            }

            /*getFlowDataLasted(userPaging, state = Lifecycle.State.CREATED) {
                Log.d("PagingElon", "---collect")
                //userPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it )
            }*/

            getFlowDataLasted(mainViewModel.sharedData) {
                //showToast("shared: $it")
            }

            getLiveData(
                findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
                    "key"
                ) ?: return@run
            ) {
                // show result from back stack
                // (it means from press system Back button)
                showToast("backStack: ${it ?: ""}")
            }
        }
    }

    override fun handleFragmentBackPressed(): (() -> Unit) = {
        // return empty unit to prevent back pressed
        showToast(this::class.simpleName)
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
                    viewModel.updateUserPaging(newUser)
                })

            userPagingAdapter = UserPagingAdapter(
                onItemLongClick = { user, _ ->
                    val newUser = user.copy(
                        name = "Medal nè"
                    )
                    viewModel.updateUserPaging(newUser)
                },
                onItemClick = { user, _ ->
                    viewModel.removeUserPaging(user)
                })


            rcvUser.apply {
                //supportChangeAnimation(false)
                layoutManager = LinearLayoutManager(context).apply {
                    orientation = RecyclerView.VERTICAL
                }

                adapter = userAdapter

                setHasFixedSize(true)
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