package com.ayakashi.kitsune.sidedroid_client.views.ipdetect

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ayakashi.kitsune.sidedroid_client.R
import com.ayakashi.kitsune.sidedroid_client.data.IPAddressItem
import com.ayakashi.kitsune.sidedroid_client.model.MainAppViewModel
import com.ayakashi.kitsune.sidedroid_client.model.ShellProcessModel
import com.ayakashi.kitsune.sidedroid_client.ui.theme.SideDroidClientTheme
import com.ayakashi.kitsune.sidedroid_client.views.ScreenShareView
import kotlinx.coroutines.*
import java.net.InetAddress
import androidx.compose.animation.core.rememberInfiniteTransition as rememberInfiniteTransition1

@Composable
fun ReachableIpAddress(
    modifier: Modifier = Modifier,
    model: MainAppViewModel = viewModel(),
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val isLoading = remember { mutableStateOf(true) }
    val isSharingscreen = remember {
        mutableStateOf(false)
    }
    val isShowSettings = remember { mutableStateOf(false) }
    val statemodel by model.uiDataModelState.collectAsState()
    val port = remember {
        mutableStateOf(8080)
    }
    val ip = remember {
        mutableStateOf("192.168.1.1")
    }

    val alertDialogShow = remember {
        mutableStateOf(false)
    }
    val platformOfServer = remember {
        mutableStateOf(1)
    }

    LaunchedEffect(key1 = true) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = withContext(Dispatchers.IO) {
                model.addIp()
            }
            isLoading.value = result
        }
    }

    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TitleBar(
                refreshAction = {
                    isLoading.value = true
                    // initiate make get ip addresses
                    CoroutineScope(Dispatchers.IO).launch {
                        val result = withContext(Dispatchers.IO) {
                            model.addIp()
                        }
                        isLoading.value = result
                    }
                },
                settingsOnclick = {
                    isShowSettings.value = !isShowSettings.value
                }
            )

        },
    ) {

        /*settings*/
        AnimatedVisibility(
            visible = isShowSettings.value,
            modifier = modifier.fillMaxSize(),
            enter = slideInVertically {
                with(density) { 800.dp.roundToPx() }
            },
        ) {
            Box(
                modifier = modifier
                    .clip(
                        shape = RoundedCornerShape(
                            40.dp, 40.dp, 0.dp, 0.dp,
                        )
                    )
                    .background(MaterialTheme.colors.onPrimary)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                   Card(
                       backgroundColor = MaterialTheme.colors.background,
                       modifier = modifier.padding(12.dp)
                   ) {
                       TextField(
                           value = ip.value,
                           onValueChange = {
                               ip.value = it
                           }
                       )
                   }
                    OutlinedButton(
                        onClick = {
                            isShowSettings.value = !isShowSettings.value
                            ip.value = "/${ip.value}"
                            Log.i("IPaddress is ", "${ip.value} ${port.value}")
                            alertDialogShow.value = !alertDialogShow.value
                        },
                    ) {
                        Text(
                            "Connect",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        /*sharing screen*/
        AnimatedVisibility(visible = isSharingscreen.value) {
            Box(
                modifier = modifier
                    .clip(
                        shape = RoundedCornerShape(
                            40.dp, 40.dp, 0.dp, 0.dp,
                        )
                    )
                    .background(MaterialTheme.colors.onPrimary),
            ) {
                if (isSharingscreen.value) {
                    ScreenShareView(
                        ip = ip.value.substring(1),
                        port = port.value,
                        onCLickBack = {
                            isSharingscreen.value = !isSharingscreen.value
                        },
                        platform = platformOfServer.value
                    )

                }
            }
        }


        /*contents card*/
        AnimatedVisibility(
            visible = !isShowSettings.value,
            enter = slideInVertically {
                with(density) { 800.dp.roundToPx() }
            },
            exit = slideOutVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) {
                with(density) { 800.dp.roundToPx() }
            }
        ) {
            Box(
                modifier = modifier
                    .clip(
                        shape = RoundedCornerShape(40.dp, 40.dp, 0.dp, 0.dp)
                    )
                    .background(MaterialTheme.colors.background),
            ) {
                /*Loading indicator*/
                AnimatedVisibility(
                    visible = isLoading.value,
                    modifier = modifier
                        .fillMaxSize()
                        .wrapContentHeight(CenterVertically)
                        .wrapContentWidth(CenterHorizontally),
                    enter = fadeIn(
                        initialAlpha = 0f
                    ),
                    exit = fadeOut(
                        targetAlpha = 0f
                    )
                ) {
                   Column() {
                       Indicator()
                   }
                }
                if (alertDialogShow.value) {
                    ShellProcessModel.setup(ip.value.substring(1),9000)
                    Custom_AlertDialog(
                        onDismiss = {
                            alertDialogShow.value = !alertDialogShow.value
                        },
                        onConfirm = {
                            CoroutineScope(Dispatchers.IO).launch {
                                platformOfServer.value = it
                                alertDialogShow.value = !alertDialogShow.value
                                isSharingscreen.value = true
                            }
                        },
                        context = context,
                    )
                }
                /*List of IP address*/
                AnimatedVisibility(
                    visible = !isLoading.value && !isSharingscreen.value,
                    enter = fadeIn(
                        initialAlpha = 0.3f
                    ),
                    exit = fadeOut(
                        targetAlpha = 0f
                    )
                ) {
                    ShowIPColumn(
                        IPAddressItemList = statemodel.IPAddressItemList,
                        onClick = {
                            ip.value = it
                            Log.i("IPaddress is ", "${ip.value} ${port.value}")
                            alertDialogShow.value = !alertDialogShow.value
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ShowIPColumn(
    IPAddressItemList: MutableList<IPAddressItem>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IPAddressItemList.forEach { obj ->
            IpText(
                ip = obj.ip,
                startSharescreen = { onClick(obj.ip) }
            )
        }
    }
}


@Composable
fun Custom_AlertDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    context: Context,
    title: String = "Enter your password",
) {
    val password = remember {
        mutableStateOf("")
    }
    val authenticating = remember {
        mutableStateOf(false)
    }
    AlertDialog(
        properties = DialogProperties(true, true),
        onDismissRequest = onDismiss,
        title = {
            Text(
                title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text("Write the password generated by the host",
            )
        },
        buttons = {
            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(horizontal = 20.dp)
            ) {
                TextField(
                    value = password.value,
                    onValueChange = {
                            password.value = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (password.value != "") {
                                val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
                                authenticating.value = !authenticating.value
                                CoroutineScope(Dispatchers.IO).launch {
                                    withContext(Dispatchers.IO) {
                                        if (ShellProcessModel.socketConnecting()) {

                                            val result =
                                                ShellProcessModel.authentication(password.value.toInt())
                                            when (result) {
                                                0 -> {
                                                    toast.show()
                                                }
                                                1 -> {
                                                    /*linux*/
                                                    onConfirm.invoke(1)
                                                    onDismiss.invoke()
                                                }
                                                2 -> {
                                                    /*windows*/
                                                    onConfirm.invoke(2)
                                                    onDismiss.invoke()
                                                }
                                                3 -> {
                                                    /*mac*/
                                                    onConfirm.invoke(3)
                                                    onDismiss.invoke()
                                                }

                                                else -> onDismiss.invoke()
                                            }
                                            Log.d(
                                                "shellprocess result auth: ",
                                                result.toString()
                                            )
                                        } else {
                                            toast.show()
                                        }
                                        authenticating.value = !authenticating.value
                                    }
                                }
                            }
                        }
                    )
                )
                if (authenticating.value) {
                    var count = 5
                    Indicator()

                } else {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (password.value != "") {
                                    val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
                                    authenticating.value = !authenticating.value
                                    CoroutineScope(Dispatchers.IO).launch {
                                        withContext(Dispatchers.IO) {
                                            if (ShellProcessModel.socketConnecting()) {
                                                val result =
                                                    ShellProcessModel.authentication(password.value.toInt())
                                                when (result) {
                                                    0 -> {
                                                        toast.show()
                                                    }
                                                    1 -> {
                                                        /*linux*/
                                                        onConfirm.invoke(1)
                                                        onDismiss.invoke()
                                                    }
                                                    2 -> {
                                                        /*windows*/
                                                        onConfirm.invoke(2)
                                                        onDismiss.invoke()
                                                    }
                                                    3 -> {
                                                        /*mac*/
                                                        onConfirm.invoke(3)
                                                        onDismiss.invoke()
                                                    }

                                                    else -> onDismiss.invoke()
                                                }
                                                Log.d(
                                                    "shellprocess result auth: ",
                                                    result.toString()
                                                )
                                            } else {
                                                toast.show()
                                            }
                                            authenticating.value = !authenticating.value
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(Color.Transparent),
                            border = BorderStroke(2.dp,MaterialTheme.colors.secondary)
                        ) {
                            Text(text = "Confirm")
                        }
                        OutlinedButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.outlinedButtonColors(Color.Transparent),
                            border = BorderStroke(2.dp,MaterialTheme.colors.secondary)
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }

        },
    )
}

@Composable
fun IpText(
    modifier: Modifier = Modifier,
    ip: String = "/192.168.1.1",
    bgcolor: Color = Color.Transparent,
    startSharescreen: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        border = BorderStroke(4.dp,MaterialTheme.colors.secondary),
        shape = RoundedCornerShape(100),
        backgroundColor = Color(0,0,0,0)
    ) {
        Row(
            modifier = modifier
                .background(Color.Transparent)
                .padding(8.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                painter = if (ip != "/192.168.1.1")
                    painterResource(id = R.drawable.devices_fill0_wght400_grad0_opsz48)
                else painterResource(id = R.drawable.router_fill0_wght400_grad0_opsz48),
                contentDescription = "icon"
            )
            Text(
                text = ip,
                modifier = modifier.padding(horizontal = 12.dp, vertical = 0.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = startSharescreen) {
                Icon(
                    imageVector = Icons.Rounded.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun TitleBar(
    modifier: Modifier = Modifier,
    title: String = "SideDroid",
    refreshAction: () -> Unit,
    settingsOnclick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        IconButton(
            onClick = refreshAction,
        ) {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = "refresh IP addresses"
            )
        }
        Text(
            text = title,
            fontSize = 24.sp
        )

        IconButton(
            onClick = settingsOnclick,
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "refresh IP addresses"
            )
        }
    }
}

@Composable
fun Indicator(
    size: Dp = 32.dp, // indicator size
    sweepAngle: Float = 90f, // angle (lenght) of indicator arc
    color: Color = MaterialTheme.colors.primary, // color of indicator arc line
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth //width of cicle and ar lines
) {
    ////// animation //////

    // docs recomend use transition animation for infinite loops
    // https://developer.android.com/jetpack/compose/animation
    val transition = rememberInfiniteTransition1()

    // define the changing value from 0 to 360.
    // This is the angle of the beginning of indicator arc
    // this value will change over time from 0 to 360 and repeat indefinitely.
    // it changes starting position of the indicator arc and the animation is obtained
    val currentArcStartAngle by transition.animateValue(
        0,
        360,
        Int.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = 1100,
                easing = LinearEasing
            )
        )
    )

    ////// draw /////

    // define stroke with given width and arc ends type considering device DPI
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
    }

    // draw on canvas
    Canvas(
        Modifier
            .progressSemantics() // (optional) for Accessibility services
            .size(size) // canvas size
            .padding(strokeWidth / 2) //padding. otherwise, not the whole circle will fit in the canvas
    ) {
        // draw "background" (gray) circle with defined stroke.
        // without explicit center and radius it fit canvas bounds
        drawCircle(Color.LightGray, style = stroke)

        // draw arc with the same stroke
        drawArc(
            color,
            // arc start angle
            // -90 shifts the start position towards the y-axis
            startAngle = currentArcStartAngle.toFloat() - 90,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = stroke
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Prev() {
    SideDroidClientTheme(
        darkTheme = true
    ) {
        IpText(
            startSharescreen = {}
        )
    }
}