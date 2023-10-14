package com.example.workmanagerandroid

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkInfo
import com.example.workmanagerandroid.presentation.BlurScreen
import com.example.workmanagerandroid.presentation.BlurViewModel
import com.example.workmanagerandroid.presentation.BlurViewModelFactory
import com.example.workmanagerandroid.ui.theme.WorkManagerAndroidTheme

class BlurActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasPermission(this, Manifest.permission.POST_NOTIFICATIONS)) {
            requestManyPermission(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
        val vm: BlurViewModel by viewModels { BlurViewModelFactory(application) }

        var showProgress by mutableStateOf(false)

        vm.outputWorkInfos.observe(this, object : Observer<List<WorkInfo>> {
            override fun onChanged(value: List<WorkInfo>) {
                if (value.isNullOrEmpty()) {
                    return
                }
                val workInfo = value[0]
                if (workInfo.state.isFinished) {
                    val outputImageUri = workInfo.outputData.getString(KEY_IMAGE_URI)
                    if (!outputImageUri.isNullOrEmpty()) {
                        vm.setOutputUri(outputImageUri)
                    }
                    showProgress = false
                } else {
                    showProgress = true
                }
            }

        })

        setContent {
            WorkManagerAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BlurScreen(
                        showProgress = showProgress,
                        blurClick = {
                            vm.applyBlur()
                            showProgress = true
                        },
                        fileClick = {
                            vm.outputUri?.let { imageUri ->
                                Intent(Intent.ACTION_VIEW, imageUri).also {
//                                    it.resolveActivity(packageManager)?.run {
                                        startActivity(it)
//                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}