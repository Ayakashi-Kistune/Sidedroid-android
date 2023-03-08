package com.ayakashi.kitsune.sidedroid_client.views

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayakashi.kitsune.sidedroid_client.model.MirrorDIsplayVIewModel
import com.ayakashi.kitsune.sidedroid_client.model.ShellProcessModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ScreenShareView(
    modifier: Modifier = Modifier,
    mirrorDIsplayVIewModel: MirrorDIsplayVIewModel = MirrorDIsplayVIewModel(),
    ip: String,
    port: Int,
    onCLickBack: () -> Unit,
    platform: Int
) {
    val modelstate by mirrorDIsplayVIewModel.uiImageDataModel.collectAsState()
    val framemodel by mirrorDIsplayVIewModel.uiframesPerSecData.collectAsState()
    val context = LocalContext.current
    var serverPlatform = when (platform) {
        1 -> "linux"
        2 -> "windows"
        else -> "mac"
    }
    LaunchedEffect(key1 = true) {
        Toast.makeText(
            context,
            "ip: $ip\nport: $port",
            Toast.LENGTH_SHORT
        ).show()
    }

    LaunchedEffect(key1 = true) {
        val isConnected = mirrorDIsplayVIewModel.startServer(ip, port)
        Log.i(
            "isconnected",
            "${isConnected.toString()} ${mirrorDIsplayVIewModel.ip} ${mirrorDIsplayVIewModel.port}"
        )
        if (isConnected == 1) {
            Toast.makeText(
                context,
                "Link Start",
                Toast.LENGTH_SHORT
            ).show()

            Toast.makeText(
                context,
                "Platform: $serverPlatform",
                Toast.LENGTH_SHORT
            ).show()
            withContext(Dispatchers.IO){
                mirrorDIsplayVIewModel.sendimage()
            }
        } else {
            Toast.makeText(
                context,
                "Not Connected",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    BackHandler(true) {
        Toast.makeText(
            context,
            "ip: $ip\nport: $port",
            Toast.LENGTH_SHORT
        ).show()
       CoroutineScope(Dispatchers.IO).launch{
           mirrorDIsplayVIewModel.stopServer()
       }
        ShellProcessModel.cutSocket()
    }

    Scaffold(
        topBar = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                   CoroutineScope(Dispatchers.IO).launch(){
                       ShellProcessModel.cutSocket()
                       mirrorDIsplayVIewModel.stopServer()
                       onCLickBack()
                   }
                }) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            Text(text =  "${(fpsaccuracy(framemodel.framesPerSecData) / 30.0 * 100.0).toInt()}%", fontSize = 32.sp)
            
            Image(
                bitmap = modelstate.ImageShared.asImageBitmap(),
                contentDescription = null,
                modifier = modifier.fillMaxWidth()
            )
            // make a custom buttons commands
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ) {
                /*launch chrome*/
                customCommand_buttons(
                    command = ShellProcessModel.command[1],
                    platform = platform,
                    icon = Icons.Rounded.Share,
                    onCLickBack = {
                            command,pltf ->
                        Toast.makeText(context, command[pltf],Toast.LENGTH_SHORT).show()

                        CoroutineScope(Dispatchers.IO).launch{
                            ShellProcessModel.sendCommand(command[pltf])
                        }
                    }
                )
                /*launch file manager*/
                customCommand_buttons(
                    command = ShellProcessModel.command[0],
                    platform = platform,
                    icon = Icons.Rounded.Home,
                    onCLickBack = {
                            command,pltf ->
                        Toast.makeText(context, command[pltf],Toast.LENGTH_SHORT).show()

                        CoroutineScope(Dispatchers.IO).launch{
                            withContext(Dispatchers.IO){
                                ShellProcessModel.sendCommand(command[pltf])
                            }
                        }
                    }
                )
                /*terminal*/
                customCommand_buttons(
                    command = ShellProcessModel.command[2],
                    platform = platform,
                    icon = Icons.Rounded.Settings,
                    onCLickBack = {
                            command,pltf ->
                        Toast.makeText(context, command[pltf],Toast.LENGTH_SHORT).show()

                        CoroutineScope(Dispatchers.IO).launch{
                            ShellProcessModel.sendCommand(command[pltf])
                        }
                    }
                )
                /*sys preference*/
                customCommand_buttons(
                    command = ShellProcessModel.command[3],
                    platform = platform,
                    icon = Icons.Rounded.Settings,
                    onCLickBack = {
                        command,pltf ->
                        Toast.makeText(context, command[pltf],Toast.LENGTH_SHORT).show()

                        CoroutineScope(Dispatchers.IO).launch{
                            ShellProcessModel.sendCommand(command[pltf])
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun customCommand_buttons(
    modifier: Modifier = Modifier,
    command: List<String>,
    platform: Int,
    icon: ImageVector,
    onCLickBack: (List<String>,Int) -> Unit
) {
    OutlinedButton(
        onClick = { onCLickBack(command, platform-1) },
        modifier = modifier.clip(RoundedCornerShape(10.dp)),
        colors = ButtonDefaults.outlinedButtonColors(Color.Transparent),
        border = BorderStroke(3.dp,MaterialTheme.colors.secondary)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = modifier
                .wrapContentSize(Alignment.Center)
                .background(Color.Transparent)
                .padding(12.dp)
        )
    }
}

private fun fpsaccuracy(fps : Double) :Int{
    return when(fps.toInt()){
        24,25,26,27,28,29,30 -> 30 //100
        21,22,23 -> 29 //96
        17,18,19,20, -> 28//93
        16,15,14, -> 27//90
        13,12,11,10,9,8,-> 26
        7,6,5,4,3,2,1,0 -> 25
        else-> fps.toInt()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    Scaffold() {

    }
}