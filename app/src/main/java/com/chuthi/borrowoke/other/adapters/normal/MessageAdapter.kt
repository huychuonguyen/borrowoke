package com.chuthi.borrowoke.other.adapters.normal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.BaseAdapter
import com.chuthi.borrowoke.data.model.GptUserRole
import com.chuthi.borrowoke.data.model.MessageModel
import com.chuthi.borrowoke.databinding.AssistantMessageItemBinding
import com.chuthi.borrowoke.databinding.UserMessageItemBinding
import com.chuthi.borrowoke.ext.gone
import com.chuthi.borrowoke.ext.loadImage
import com.chuthi.borrowoke.ext.onSafeClick
import com.chuthi.borrowoke.ext.show
import com.chuthi.borrowoke.ext.toggleSlide

/**
 * MessageAdapter.
 */
class MessageAdapter(
    private val onItemClicked: (MessageModel) -> Unit
) : BaseAdapter<MessageModel, ViewBinding>(
    UserMessageItemBinding::inflate
) {
    override fun onItemClicked(binding: ViewBinding, item: MessageModel, position: Int) {
    }

    override fun onBindData(binding: ViewBinding, item: MessageModel, position: Int) {
        val name = when (item.role) {
            is GptUserRole.User -> "You"
            is GptUserRole.Assistant -> "Assistant"
            is GptUserRole.System -> "System"
        }
        when (binding) {
            is UserMessageItemBinding -> binding.run {
                messageItem.run {
                    tvMessageDesc.run {
                        text = root.context.getString(
                            R.string.format_2_param, name,
                            item.date
                        )
                        val visibleType =
                            if (item.isVisibleDate) View.VISIBLE
                            else View.GONE
                        this.toggleSlide(visibleType)
                    }
                    tvContent.text = item.content

                    if (item.url.isNullOrEmpty()) cvContent.gone()
                    else cvContent.run {
                        show()
                        ivContent.loadImage(item.url)
                    }

                    cvUser.onSafeClick {
                        onItemClicked.invoke(item)
                    }
                }
            }

            is AssistantMessageItemBinding -> binding.run {
                messageItem.run {
                    tvMessageDesc.run {
                        text = root.context.getString(
                            R.string.format_2_param, name,
                            item.date
                        )
                        val visibleType =
                            if (item.isVisibleDate) View.VISIBLE
                            else View.GONE
                        this.toggleSlide(visibleType)
                    }
                    tvContent.text = item.content

                    if (item.url.isNullOrEmpty()) cvContent.gone()
                    else cvContent.run {
                        show()
                        ivContent.loadImage(item.url)
                    }

                    cvAssistant.onSafeClick {
                        onItemClicked.invoke(item)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = when (viewType) {
            TYPE_USER -> UserMessageItemBinding.inflate(inflater, parent, false)
            TYPE_ASSISTANT -> AssistantMessageItemBinding.inflate(inflater, parent, false)
            else -> UserMessageItemBinding.inflate(inflater, parent, false)
        }
        return BaseHolder(binding)
    }

    /**
     * return the layout id of each item view
     */
    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item.role) {
            GptUserRole.System -> TYPE_SYSTEM
            GptUserRole.User -> TYPE_USER
            GptUserRole.Assistant -> TYPE_ASSISTANT
        }
    }

    companion object {
        private const val TYPE_SYSTEM = 0
        private const val TYPE_USER = 1
        private const val TYPE_ASSISTANT = 2
    }
}