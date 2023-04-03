package com.chuthi.borrowoke.data.repo

import com.chuthi.borrowoke.base.BaseRepo
import com.chuthi.borrowoke.data.api.ApiService

class NewsRepo(
    private val apiService: ApiService
) : BaseRepo() {

    suspend fun getBreakingNews() = apiService.getBreakingNews()
}