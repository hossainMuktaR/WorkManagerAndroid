package com.example.workmanagerandroid

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainViewModel(
    application: Application
): ViewModel() {

    private val workManager = WorkManager.getInstance(application)
    private val DRINK_NOTIFY_WORK_NAME = "drinknotifywork"
    private val DrinkWorkRequestTag = "DrinkRequestTag"

    val drinkWorkInfos: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(
        DrinkWorkRequestTag)

    internal fun cancleWork() {
        workManager.cancelUniqueWork(DRINK_NOTIFY_WORK_NAME)
    }
    fun startDrinkNotifyWorker(){
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<DrinkNotifyWorker>(30, TimeUnit.MINUTES)
            .addTag(DrinkWorkRequestTag)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            DRINK_NOTIFY_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
            )
    }
}

class MainViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}