package com.chuthi.borrowoke.data.repo

import com.chuthi.borrowoke.base.BaseRepo
import com.chuthi.borrowoke.data.api.ApiService

class DogRepo(private val apiService: ApiService) : BaseRepo() {
    suspend fun getDogs() = apiService.getDogs()
}