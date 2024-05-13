package com.chuthi.borrowoke.other.adapters.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import com.chuthi.borrowoke.base.paging.BasePagingAdapter
import com.chuthi.borrowoke.data.model.UserModel
import com.chuthi.borrowoke.databinding.UserItemBinding

class UserPagingAdapter(
    private val onItemClick: (item: UserModel, position: Int) -> Unit,
    private val onItemLongClick: (item: UserModel, position: Int) -> Unit
) : BasePagingAdapter<UserModel, UserItemBinding>(
    areItemsTheSame = areItemsTheSame,
    areContentsTheSame = { old, new ->
        old == new
    }
) {

    override fun setViewBinding(parent: ViewGroup) = UserItemBinding
        .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun onItemClicked(binding: UserItemBinding, item: UserModel, position: Int) {
        onItemClick.invoke(item, position)
    }

    override fun onBindData(binding: UserItemBinding, item: UserModel, position: Int) {
        binding.run {
            root.setOnLongClickListener {
                onItemLongClick.invoke(item, position)
                false
            }

            tvUserId.text = item.userId.toString()
            tvUsername.text = item.name
        }
    }

    companion object {
        val areItemsTheSame = { old: UserModel, new: UserModel ->
            old.userId == new.userId
        }
    }

}