package com.example.workmanagerandroid.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanagerandroid.OUTPUT_PATH
import com.example.workmanagerandroid.makeStatusNotification
import com.example.workmanagerandroid.sleep
import java.io.File

private val TAG = "cleanupWorker"
class CleanupWorker(
    context: Context,
    params: WorkerParameters
): Worker(context, params) {

    override fun doWork(): Result {
        makeStatusNotification("cleaning up old temporary files", applicationContext)
        //thread sleep 3 second to slow process to user see notification
        sleep()
        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if(outputDirectory.exists()){
                val entries = outputDirectory.listFiles()
                if(entries != null) {
                    for(entry in entries) {
                        val name = entry.name
                        if(name.isNotEmpty() && name.endsWith(".png")){
                            val delete = entry.delete()
                            Log.i(TAG, "Deleted $name - $delete")
                        }
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}