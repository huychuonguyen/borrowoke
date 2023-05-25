package com.chuthi.borrowoke.other.adapters.normal

import com.chuthi.borrowoke.base.BaseAdapter
import com.chuthi.borrowoke.data.model.UserModel
import com.chuthi.borrowoke.databinding.UserItemBinding
import com.chuthi.borrowoke.other.enums.asString

class UserAdapter(
    private val onItemClick: (item: UserModel, position: Int) -> Unit,
    private val onItemLongClick: (item: UserModel, position: Int) -> Unit
) : BaseAdapter<UserModel, UserItemBinding>(UserItemBinding::inflate) {

    override fun onItemClicked() = { _: UserItemBinding, item: UserModel, position: Int ->
        onItemClick.invoke(item, position)
    }

    override fun onBindData(binding: UserItemBinding, item: UserModel, position: Int) {
        binding.run {
            root.setOnLongClickListener {
                onItemLongClick.invoke(item, position)
                false
            }

            tvUserId.text = item.userId.toString()
            tvUsername.text = item.value.asString(root.context)
        }
    }
}