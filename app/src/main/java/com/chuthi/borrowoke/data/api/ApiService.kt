package com.chuthi.borrowoke.data.api

import com.chuthi.borrowoke.data.model.response.NewsResponse
import com.chuthi.borrowoke.other.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 2,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>
}