package com.example.workmanagerandroid.presentation

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.workmanagerandroid.IMAGE_MANIPULATION_WORK_NAME
import com.example.workmanagerandroid.KEY_IMAGE_URI
import com.example.workmanagerandroid.R
import com.example.workmanagerandroid.TAG_OUTPUT
import com.example.workmanagerandroid.workers.BlurWorker
import com.example.workmanagerandroid.workers.CleanupWorker
import com.example.workmanagerandroid.workers.SaveImageToFileWorker

class BlurViewModel(
    application: Application
): ViewModel() {
    private var imageUri: Uri? = null
    internal var outputUri: Uri? = null
    private val workManager = WorkManager.getInstance(application)

    internal val outputWorkInfos: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(
        TAG_OUTPUT)

    init {
        imageUri = getImageUri(application.applicationContext)
    }
    internal fun cancleWork() {
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }
    fun applyBlur() {
        var continuation = workManager
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java))
        val blurRequest = OneTimeWorkRequestBuilder<BlurWorker>()
            .setInputData(createInputDataForUri())
            .build()
        continuation = continuation.then(blurRequest)
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()
        val saveRequest = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .setConstraints(constraints)
            .addTag(TAG_OUTPUT)
            .build()
        continuation = continuation.then(saveRequest)

//        actualy start the work
        continuation.enqueue()
    }


    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }
        return builder.build()
    }

    private fun getImageUri(context: Context): Uri {
        val resources = context.resources
        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceTypeName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceEntryName(R.drawable.android_cupcake))
            .build()
    }

    internal fun setOutputUri(outputImageUri: String) {
        outputUri = Uri.parse(outputImageUri)
    }


}

class BlurViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(BlurViewModel::class.java)) {
            BlurViewModel(application) as T
        }else throw IllegalArgumentException("unknown viewModel class")
    }
}