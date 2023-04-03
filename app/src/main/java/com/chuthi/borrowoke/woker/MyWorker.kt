package com.chuthi.borrowoke.woker

import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.*

class MyWorker(val workManager: WorkManager) {

    fun applyBlurWork(workInput: Data) {
        val blurWorkRequest = OneTimeWorkRequestBuilder<BlurWorker>()
            .setInputData(workInput)
            .setId(blurWorkRequestId)
            .build()
        workManager.enqueueUniqueWork(
            BlurWorker.TAG,
            ExistingWorkPolicy.REPLACE,
            blurWorkRequest
        )
    }

    /**
     * Run [NewsWorker]
     */
    fun runNewsWork() {
        val newsWorkerRequest = OneTimeWorkRequestBuilder<NewsWorker>()
            .setId(newsWorkRequestId)
            .build()
        workManager.enqueueUniqueWork(
            NewsWorker.TAG,
            ExistingWorkPolicy.REPLACE,
            newsWorkerRequest
        )
    }

    companion object {
        val blurWorkRequestId: UUID = UUID.randomUUID()
        val newsWorkRequestId: UUID = UUID.randomUUID()
    }
}