package com.ayakashi.kitsune.sidedroid_client.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.ayakashi.kitsune.sidedroid_client.data.IPAddressItem
import kotlinx.coroutines.awaitAll
import java.net.InetAddress
import java.net.NetPermission
import java.net.NetworkInterface



/*  simplify code just to make it simple AF... */
private val delay = 50
fun searchIP(
    list: MutableList<IPAddressItem>,
    startCount: Int,
    tag : String = "VIEW-MODEL"
){
    Log.d(tag,"launch")
    for (i in startCount..startCount+84) {
        val ip: InetAddress = InetAddress.getByName("192.168.1.${i}")
        if (ip.isReachable(delay)){
            Log.d(tag,"ip:${ip}")
            list.add(
                IPAddressItem(
                    ip = ip.toString(),
                    otherinfo = ""
                )
            )
        }
    }
}

fun ByteArrayToBitmap(byteArrayImage: ByteArray): Bitmap {
    return  BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage.size)
}
