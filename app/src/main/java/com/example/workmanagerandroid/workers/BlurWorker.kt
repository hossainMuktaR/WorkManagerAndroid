package com.example.workmanagerandroid.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.workmanagerandroid.DELAY_TIME_MILLIS
import com.example.workmanagerandroid.KEY_IMAGE_URI
import com.example.workmanagerandroid.R
import com.example.workmanagerandroid.blurBitmap
import com.example.workmanagerandroid.makeStatusNotification
import com.example.workmanagerandroid.sleep
import com.example.workmanagerandroid.writeBitmapToFile

private const val TAG = "BlurWorker"
class BlurWorker(
    context: Context,
    params: WorkerParameters
    ): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val appContext = applicationContext
        val resourcesUri = inputData.getString(KEY_IMAGE_URI)
        makeStatusNotification("Bluring Image", appContext)
        //thread sleep 3 second to slow process to user see notification
        sleep()
        return try {
            if (TextUtils.isEmpty(resourcesUri)){
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }
            val resolver = appContext.contentResolver
            val imageBitmap = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourcesUri))
            )
            val output = blurBitmap(imageBitmap, 3)
            val outputUri = writeBitmapToFile(appContext, output)
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
            Result.success(outputData)
        }catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")
            Result.failure()
        }
    }
}