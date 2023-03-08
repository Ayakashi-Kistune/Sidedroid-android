package com.ayakashi.kitsune.sidedroid_client.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ayakashi.kitsune.sidedroid_client.data.FramesPerSecData
import com.ayakashi.kitsune.sidedroid_client.data.ImageDataModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.DataInputStream
import java.net.Socket
import java.time.LocalTime

class MirrorDIsplayVIewModel() : ViewModel() {
    private val imageModelState = MutableStateFlow(ImageDataModel())
    val uiImageDataModel: StateFlow<ImageDataModel> = imageModelState.asStateFlow()

    private val framesPerSecData = MutableStateFlow(FramesPerSecData())
    val uiframesPerSecData: StateFlow<FramesPerSecData> = framesPerSecData.asStateFlow()
    var isConnected = false
    var socket: Socket? = ShellProcessModel.socket
    private var receiver = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null
    var ip: String? = null
    var port: Int? = null
    var frame = 0
    var countdown = 10

    fun stopServer() {
        socket?.close()
        job?.cancel()
    }

    fun startServer(ip: String, port: Int): Int {
        this.ip = ip
        this.port = port
        var i = 10
        var result = 3
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                try {
                    socket = Socket(ip, port)

                    if (socket?.isConnected == true) {
                        result = 1
                        break
                    }
                } catch (er: Exception) {
                    delay(2000)
                    Log.i("Startserver", "exception failed connection ${er.message}")
                    if (i == 0) {
                        result = 0
                        break
                    }
                    i--
                }
            }
            cancel()
        }
        while (result == 3) {

        }
        return result
    }

    fun sendimage() {
        job = receiver.launch {
            withContext(Dispatchers.IO){
                var time = LocalTime.now()
                while (true) {
                    try {
                        val data = DataInputStream(socket!!.getInputStream())
                        while (true) {
                            val size = data.readInt()
//                            Log.i("sendimage", "$size")

                            var arr = ByteArray(size)
//                            Log.i("sendimage", "array made")
                            data.readFully(arr)

//                            println("updating")
                            imageModelState.update {
                                ImageDataModel(
                                    ByteArrayToBitmap(arr)
                                )
                            }
//                            println("shown image $frame")
                            frame++
                            val newtime = LocalTime.now()
                            if (time.plusSeconds(1) < newtime) {
                                time = newtime
                                framesPerSecData.update {
                                    FramesPerSecData(frame.toDouble())
                                }
                                frame = 0
                            }
                        }
                    } catch (er: java.lang.Exception) {
                        while(countdown != 1 ) {
                            delay(2000)
                            try {
                                println("reconnecting ${er}")
                                withContext(Dispatchers.IO){
                                    socket = port?.let { it1 -> Socket(ip, it1) }
                                }
                                if (socket!!.isConnected) {
                                    break
                                }

                            } catch (er: Exception) {
                                println("failed")
                            }
                            countdown--
                        }
                        countdown = 10
                        isConnected = !isConnected
                    }
                }
            }
        }
    }
}
