package com.chuthi.borrowoke.other.adapters.normal

import android.view.LayoutInflater
import android.view.ViewGroup
import com.chibijob.ui.base.BaseAdapter
import com.chuthi.borrowoke.data.model.UserModel
import com.chuthi.borrowoke.databinding.UserItemBinding

class UserAdapter(
    private val onItemClick: (item: UserModel, position: Int) -> Unit,
    private val onItemLongClick: (item: UserModel, position: Int) -> Unit
) : BaseAdapter<UserModel, UserItemBinding>() {

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
}