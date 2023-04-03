package com.chuthi.borrowoke.woker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chuthi.borrowoke.data.repo.NewsRepo
import com.chuthi.borrowoke.ext.apiCall

class NewsWorker(
    private val newsRepo: NewsRepo,
    val context: Context,
    val params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            newsRepo.getBreakingNews().apiCall(
                onSuccess = {
                    val articles = it.data?.articles ?: emptyList()
                    Log.i("NewsWorker_Success", articles.toString())
                    Result.success()
                },
                onException = {
                    Log.i("NewsWorker_Failure", "fail")
                    Result.failure()
                }
            )
            Result.success()
        } catch (Ex: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val TAG = "NewsWorker"
    }
}