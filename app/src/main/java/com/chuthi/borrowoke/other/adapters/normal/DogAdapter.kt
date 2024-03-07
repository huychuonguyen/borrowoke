package com.chuthi.borrowoke.other.adapters.normal

import com.chuthi.borrowoke.base.BaseAdapter
import com.chuthi.borrowoke.data.model.response.DogResponse
import com.chuthi.borrowoke.databinding.DogItemBinding
import com.chuthi.borrowoke.ext.loadImage

class DogAdapter(
    private val onItemClicked: (String) -> Unit,
) : BaseAdapter<DogResponse, DogItemBinding>(
    DogItemBinding::inflate
) {
    override fun onItemClicked() = { _: DogItemBinding, item: DogResponse, _: Int ->
        onItemClicked.invoke(item.url)
    }

    override fun onBindData(binding: DogItemBinding, item: DogResponse, position: Int) {
        binding.run {
            ivDog.loadImage(item.url)
            tvUrl.text = item.url.split('.').lastOrNull() ?: ""
        }
    }
}