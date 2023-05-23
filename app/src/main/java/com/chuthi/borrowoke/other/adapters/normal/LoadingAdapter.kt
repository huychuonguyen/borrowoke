package com.chuthi.borrowoke.other.adapters.normal

import com.chuthi.borrowoke.base.BaseAdapter
import com.chuthi.borrowoke.data.model.LoadingModel
import com.chuthi.borrowoke.databinding.MessageLoadingItemBinding
import com.chuthi.borrowoke.ext.gone
import com.chuthi.borrowoke.ext.show
import com.chuthi.borrowoke.other.enums.asString

class LoadingAdapter(private val onItemClicked: () -> Unit) :
    BaseAdapter<LoadingModel, MessageLoadingItemBinding>(
        MessageLoadingItemBinding::inflate
    ) {
    override fun onItemClicked(
        binding: MessageLoadingItemBinding,
        item: LoadingModel,
        position: Int
    ) {
        onItemClicked.invoke()
    }

    override fun onBindData(binding: MessageLoadingItemBinding, item: LoadingModel, position: Int) {
        binding.loadingItem.run {
            when (item.loadingState) {
                is LoadingModel.LoadingState.Loading -> binding.run {
                    root.show()
                    loadingItem.progressBar.show()
                }

                is LoadingModel.LoadingState.None -> binding.root.gone()
                is LoadingModel.LoadingState.Error -> binding.loadingItem.progressBar.gone()
            }
            tvContent.text = item.content.asString(root.context)
        }
    }
}