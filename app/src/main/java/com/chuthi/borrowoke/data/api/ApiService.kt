package com.chuthi.borrowoke.data.api

import com.chuthi.borrowoke.data.model.request.ChatGptRequest
import com.chuthi.borrowoke.data.model.response.DogResponse
import com.chuthi.borrowoke.data.model.response.NewsResponse
import com.chuthi.borrowoke.other.API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v1/images/search")
    suspend fun getDogs(
        @Query("size")
        size: String = "small",
        @Query("page")
        pageNumber: Int = 1,
        @Query("limit")
        limit: Int = 25
    ): Response<List<DogResponse>>

    // chatGPT
    //@Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun chatGPTCompletions(
        @Body request: ChatGptRequest
    ): Response<Any>

}