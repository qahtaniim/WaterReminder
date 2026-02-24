package com.example.waterreminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private val requestNotifPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ -> }

    private fun askNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= 33) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                requestNotifPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askNotificationPermissionIfNeeded()
        NotificationHelper.ensureChannel(this)

        setContent {
            MaterialTheme {
                MainScreen(
                    onTestNotify = {
                        NotificationHelper.show(
                            this,
                            "💧 تذكير الماء",
                            "هذا إشعار تجريبي"
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun MainScreen(onTestNotify: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onTestNotify) {
            Text("اختبار إشعار الآن")
        }
    }
}