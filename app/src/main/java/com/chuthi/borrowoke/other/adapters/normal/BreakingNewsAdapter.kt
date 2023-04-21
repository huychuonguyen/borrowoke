package com.chuthi.borrowoke.other.adapters.normal

import com.chuthi.borrowoke.base.BaseAdapter
import com.chuthi.borrowoke.data.model.response.Article
import com.chuthi.borrowoke.databinding.ArticleItemBinding

class BreakingNewsAdapter(
    private val onItemClicked: (ArticleItemBinding, Article) -> Unit
) : BaseAdapter<Article, ArticleItemBinding>(
    ArticleItemBinding::inflate
) {

    override fun onBindData(binding: ArticleItemBinding, item: Article, position: Int) {
        binding.run {
            tvAuthor.text = item.author ?: "Đéo có tác giả"
            tvContent.text = item.content ?: "Đéo có nội dung"
        }
    }

    override fun onItemClicked(binding: ArticleItemBinding, item: Article, position: Int) {
        onItemClicked.invoke(binding, item)
    }
}