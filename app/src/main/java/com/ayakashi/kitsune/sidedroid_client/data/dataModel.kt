package com.ayakashi.kitsune.sidedroid_client.data

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

data class IPAddressItem(
    val ip: String,
    val otherinfo: String
)


data class IPAddressDataModel(
    val IPAddressItemList: MutableList<IPAddressItem> = mutableListOf<IPAddressItem>(),
)

data class FramesPerSecData(
    val framesPerSecData: Double = 0.0
)

data class ImageDataModel(
    val ImageShared: Bitmap =
        Bitmap.createBitmap(
            IntArray(1980 * 1080) {255 and 0xff shl 24 or (0 and 0xff shl 16) or (0 and 0xff shl 8) or (0 and 0xff) },
            1980,
            1080,
            Bitmap.Config.ARGB_8888)
)

