package com.chuthi.borrowoke.woker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.chuthi.borrowoke.data.model.UserModel
import com.chuthi.borrowoke.data.model.toUserEntity
import com.chuthi.borrowoke.data.repo.UserRepo
import com.chuthi.borrowoke.other.INPUT_BLUR_WORKER
import com.chuthi.borrowoke.other.OUTPUT_BLUR_WORKER
import com.chuthi.borrowoke.util.notifyImageBlurred
import com.chuthi.borrowoke.util.notifyImageBlurring
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class BlurWorker(
    private val userRepo: UserRepo,
    val context: Context,
    val params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                setProgress(workDataOf(PROGRESS to 0))

                Log.i("blur_worker", "Start blur work")
                val inputData = inputData.getString(INPUT_BLUR_WORKER) ?: "nothing"
                Log.i("blur_worker_input", inputData)

                val appContext = applicationContext
                var progress = 0
                while (true) {
                    // notify image blurring
                    notifyImageBlurring(
                        context = appContext,
                        title = "Image Blurring",
                        message = "Your image is blurring...",
                        progress = progress
                    )
                    // delay 1s on first notify
                    if (progress == 0)
                        delay(1000)
                    else delay(500)
                    progress += 10

                    if (progress >= 100) {
                        delay(1000)
                        // notify image blurre
                        notifyImageBlurred(
                            context = appContext,
                            title = "Image blurred",
                            message = "Your image has been blurred"
                        )

                        Log.i("blur_worker", "End blur work")
                        break
                    }
                }

                setProgress(workDataOf(PROGRESS to 100))

                userRepo.deleteUser(userId = 15)

                val outputData = workDataOf(OUTPUT_BLUR_WORKER to "Blur xong gòi")
                return@withContext Result.success(outputData)

            } catch (e: Exception) {
                Log.i("blur_worker", "Error blur work")
                Result.failure()
            }
        }
    }

    companion object {
        const val TAG = "BlurWorker"
        const val PROGRESS = "Progress"
    }
}