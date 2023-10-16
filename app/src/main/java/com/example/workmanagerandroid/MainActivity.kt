package com.example.workmanagerandroid

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import com.example.workmanagerandroid.ui.theme.WorkManagerAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!hasPermission(android.Manifest.permission.POST_NOTIFICATIONS)) {
            requestManyPermission(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        val vm: MainViewModel by viewModels { MainViewModelFactory(application) }
        var isWorkEnqueue by mutableStateOf(false)

        vm.drinkWorkInfos.observe(this
        ) { value ->
            if(!value.isEmpty() && value[0].state == WorkInfo.State.ENQUEUED){
                isWorkEnqueue = true
                Log.i("mainActivity", "workInfo Found")
            }else {
                isWorkEnqueue = false
                Log.i("mainActivity", "workInfo not Found")
            }
        }
        setContent {
            WorkManagerAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(if(isWorkEnqueue) "Enqueued" else "Not Running")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            enabled = !isWorkEnqueue,
                            onClick = { vm.startDrinkNotifyWorker()}
                        ){
                            Text(text = "Start Worker")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            enabled = isWorkEnqueue,
                            onClick = {vm.cancleWork()}
                        ){
                            Text(text = "Stop Worker")
                        }

                    }

                }
            }
        }
    }
    private fun hasPermission(permissionName: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            permissionName
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestManyPermission(arrayOfPermissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(
            this,
            arrayOfPermissions,
            requestCode
        )
    }
}