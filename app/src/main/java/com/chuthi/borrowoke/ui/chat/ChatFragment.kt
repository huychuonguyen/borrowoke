package com.chuthi.borrowoke.ui.chat

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.BaseFragment
import com.chuthi.borrowoke.databinding.FragmentChatBinding
import com.chuthi.borrowoke.ext.copyText
import com.chuthi.borrowoke.ext.getFlowData
import com.chuthi.borrowoke.ext.getFlowDataLasted
import com.chuthi.borrowoke.ext.getLiveData
import com.chuthi.borrowoke.ext.gone
import com.chuthi.borrowoke.ext.loadImage
import com.chuthi.borrowoke.ext.onSafeClick
import com.chuthi.borrowoke.ext.scrollToEnd
import com.chuthi.borrowoke.ext.setupLinearLayout
import com.chuthi.borrowoke.ext.showSnackBar
import com.chuthi.borrowoke.ext.supportChangeAnimation
import com.chuthi.borrowoke.ext.toggleSlide
import com.chuthi.borrowoke.other.adapters.normal.ChatAdapter
import com.chuthi.borrowoke.other.enums.ChatUiState
import com.chuthi.borrowoke.other.enums.MessageType
import com.chuthi.borrowoke.other.enums.asString
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>() {

    private lateinit var chatAdapter: ChatAdapter

    override val viewModel by viewModel<ChatViewModel>()

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentChatBinding.inflate(inflater, container, false)

    override fun setupUI() {
        binding.run {

            chatTitle.onSafeClick { }

            ivChatTitle.loadImage(drawableRes = R.drawable.logo_gumi)

            frTitle.onSafeClick(defaultInterval = 0L) {
                val visible =
                    if (tvPunchStart.visibility == View.VISIBLE)
                        View.GONE
                    else View.VISIBLE

                toggleTitle(visible)
            }

            btnSend.onSafeClick {
                val question = edtChat.text?.toString()
                question ?: return@onSafeClick
                // clear question
                edtChat.text?.clear()
                // send question
                sendQuestion(question)
            }

            tvReset.onSafeClick {
                it.showSnackBar("Reset chat?", actionText = "Reset", action = {
                    viewModel.resetChat()
                })
            }

            // reset chat
            edtChat.doOnTextChanged { text, _, _, _ ->
                viewModel.validateInput(text?.toString())
            }
        }

        setupRecyclerView()
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun toggleTitle(visible: Int) {
        binding.run {
            tvPunchStart.toggleSlide(visible, slideEge = Gravity.START,
                onFinish = {
                    tvPunchEnd.toggleSlide(visible, slideEge = Gravity.END)
                    tvCost.visibility = visible
                }
            )
        }
    }

    private fun setupRecyclerView() {
        binding.run {
            // init adapter
            chatAdapter = ChatAdapter(onMessageClicked = {
                // set visibility date item
                viewModel.setItemVisibleDate(
                    item = it, isVisible = !it.isVisibleDate
                )
            }, onItemLongClicked = {
                // copy content to clipboard
                context?.copyText(it.content)
            }, onLoadingClicked = {})
            // config recycler view
            rcvMessage.setupLinearLayout(chatAdapter.instance).supportChangeAnimation(false)
        }
    }

    private fun sendQuestion(question: String) = viewModel.sendQuestion(question)

    override fun onArgumentsSaved(arguments: Bundle?) {
    }

    override fun observeFlowData(): (CoroutineScope.() -> Unit) = {
        viewModel.run {
            getFlowDataLasted(validInput) { isValid ->
                toggleSendButton(isValid)
            }

            getFlowDataLasted(messages) {
                chatAdapter.submitList(it)
            }

            getFlowData(recentTokens) {
                binding.tvPunchEnd.text = getString(R.string.new_token, it)
            }

            getFlowData(totalTokens) {
                binding.tvPunchStart.text = getString(R.string.total_token, it)
                toggleTitle(View.VISIBLE)
            }

            getFlowData(cost) {
                binding.tvCost.text = getString(R.string.cost_per_tokens, it)
            }
        }
    }

    override fun observeLiveData(): (LifecycleOwner.() -> Unit) = {
        getLiveData(viewModel.chatUiState) {
            when (it) {
                is ChatUiState.NewMessage -> {
                    val smoothScroll = when (it.type) {
                        is MessageType.Response -> true
                        else -> false
                    }
                    binding.rcvMessage.scrollToEnd(isSmoothScroll = smoothScroll)
                }

                is ChatUiState.Loading -> {
                    chatAdapter.setLoading(it.isLoading)
                    if (it.isLoading) {
                        toggleTitle(View.GONE)
                        binding.rcvMessage.scrollToEnd(isSmoothScroll = false)
                    }
                }

                is ChatUiState.LoadError -> chatAdapter.setError(
                    it.message.asString(context),
                    it.src
                )

                is ChatUiState.Resetting -> {
                    toggleTitle(View.GONE)
                    chatAdapter.setResetting(it.message.asString(context))
                }

                is ChatUiState.None -> chatAdapter.setLoading(false)
            }
        }
    }

    private fun toggleSendButton(isVisible: Boolean) {
        binding.run {
            when (isVisible) {
                false -> cvSend.gone()
                else -> cvSend.toggleSlide(View.VISIBLE, slideEge = Gravity.END)
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ChatFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}