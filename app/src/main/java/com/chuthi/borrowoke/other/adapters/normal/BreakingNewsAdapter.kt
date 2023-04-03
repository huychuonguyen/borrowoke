package com.chuthi.borrowoke.other.adapters.normal

import android.view.LayoutInflater
import android.view.ViewGroup
import com.chibijob.ui.base.BaseAdapter
import com.chuthi.borrowoke.data.model.response.Article
import com.chuthi.borrowoke.databinding.ArticleItemBinding

class BreakingNewsAdapter : BaseAdapter<Article, ArticleItemBinding>() {

    override fun setViewBinding(parent: ViewGroup) = ArticleItemBinding
        .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun onBindData(binding: ArticleItemBinding, item: Article, position: Int) {
        binding.run {
            tvAuthor.text = item.author ?: ""
            tvContent.text = item.content ?: ""
        }
    }

    override fun onItemClicked(binding: ArticleItemBinding, item: Article, position: Int) {
    }
}