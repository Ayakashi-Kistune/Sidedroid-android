package com.ayakashi.kitsune.sidedroid_client

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ayakashi.kitsune.sidedroid_client.model.ShellProcessModel
import com.ayakashi.kitsune.sidedroid_client.ui.theme.SideDroidClientTheme
import com.ayakashi.kitsune.sidedroid_client.views.ScreenShareView
import com.ayakashi.kitsune.sidedroid_client.views.ipdetect.ReachableIpAddress

class MainActivity : ComponentActivity() {
    val tag = "main"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            SideDroidClientTheme {
                ReachableIpAddress()
//                ScreenShareView(ip = "192.168.1.12", port = 8080, platform = 1, shell = ShellProcessModel("",0), onCLickBack = {})
            }
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "started activity")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "resume activity")

    }

    override fun onRestart() {
        super.onRestart()
        Log.d(tag, "restart activity")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "destory activity")
    }
}