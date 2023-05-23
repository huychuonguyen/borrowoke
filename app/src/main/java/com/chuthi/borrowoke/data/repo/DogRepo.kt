package com.chuthi.borrowoke.data.repo

import com.chuthi.borrowoke.base.BaseRepo
import com.chuthi.borrowoke.data.api.ApiService

class DogRepo(private val apiService: ApiService) : BaseRepo() {
    suspend fun getDogs(limit: Int = 10) = apiService.getDogs(limit = limit)
}