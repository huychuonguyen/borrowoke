package com.chuthi.borrowoke.data.model.response

import com.chuthi.borrowoke.base.BaseModel

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
) : BaseModel()

data class Article(
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : BaseModel()

data class Source(
    val id: Any,
    val name: String
)