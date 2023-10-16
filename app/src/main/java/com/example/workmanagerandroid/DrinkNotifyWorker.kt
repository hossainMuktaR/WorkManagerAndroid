package com.example.workmanagerandroid

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class DrinkNotifyWorker(
    private val context: Context,
    params: WorkerParameters
): Worker(context, params) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    override fun doWork(): Result {
         notificationManager.notify(1, createNotification(context))
        return Result.success()
    }
    private fun createNotification(context: Context): Notification {
        return NotificationCompat.Builder(context, "drinkNotifyChannel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Time to Drink")
            .setContentText("Drink 1/2 Liter of water")
            .build()
    }
}