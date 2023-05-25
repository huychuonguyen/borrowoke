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

    ) = { _: MessageLoadingItemBinding,
          _: LoadingModel,
          _: Int ->
        onItemClicked.invoke()
    }

    override fun onBindData(binding: MessageLoadingItemBinding, item: LoadingModel, position: Int) {
        binding.loadingItem.run {
            when (item.loadingState) {
                is LoadingModel.LoadingState.Loading -> {
                    root.show()
                    ivIcon.gone()
                    progressBar.show()
                }

                is LoadingModel.LoadingState.None -> root.gone()
                is LoadingModel.LoadingState.Error -> {
                    root.show()
                    ivIcon.show()
                    progressBar.gone()
                    item.icSrc ?: return@run
                    ivIcon.setImageResource(item.icSrc)
                }
            }
            tvContent.text = item.content.asString(root.context)
        }
    }
}