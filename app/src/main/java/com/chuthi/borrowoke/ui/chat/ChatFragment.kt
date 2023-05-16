package com.chuthi.borrowoke.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.chuthi.borrowoke.base.BaseFragment
import com.chuthi.borrowoke.databinding.FragmentChatBinding
import com.chuthi.borrowoke.ext.getFlowDataLasted
import com.chuthi.borrowoke.ext.gone
import com.chuthi.borrowoke.ext.onSafeClick
import com.chuthi.borrowoke.ext.showToast
import com.chuthi.borrowoke.ext.toggleSlideEndStart
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>() {


    override val viewModel by viewModel<ChatViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentChatBinding.inflate(inflater, container, false)

    override fun setupUI() {
        binding.run {

            chatTitle.onSafeClick { }

            btnSend.onSafeClick {
                val question = edtChat.text?.toString()
                showToast(question)
                // clear question
                edtChat.text?.clear()
            }

            edtChat.doOnTextChanged { text, _, _, _ ->
                viewModel.validateInput(text?.toString())
            }
        }
    }

    override fun onArgumentsSaved(arguments: Bundle?) {
    }

    override fun observeFlowData(): (CoroutineScope.() -> Unit) = {
        getFlowDataLasted(viewModel.validInput) { isValid ->
            toggleSendButton(isValid)
        }
    }

    private fun toggleSendButton(isVisible: Boolean) {
        binding.run {
            isVisible.let { visibility ->
                when (visibility) {
                    false -> cvSend.gone()
                    else -> cvSend.toggleSlideEndStart(View.VISIBLE)
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ChatFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}