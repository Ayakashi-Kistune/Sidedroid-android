package com.ayakashi.kitsune.sidedroid_client.model

import android.util.Log
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

object ShellProcessModel {
    private var IP_address: String = ""
    private var port: Int =  9000
    var socket: Socket? = null
    private var dataOutputStream: DataOutputStream? = null
    private var dataInputStream: DataInputStream? = null
    // linux windows mac

    // command is
    // keycuts linux keybtn keybtn
    // cmd linux command command
    var command : MutableList<MutableList<String>> = mutableListOf(
        /*file manager*/
        mutableListOf("nautilus","keycuts ctrl E","open Downloads"),
        /*open browser*/
        mutableListOf("google-chrome","start chrome","open \"http://www.google.com\""),
        /*command promt*/
        mutableListOf("gnome-terminal","start cmd","open -a Terminal ."),
        /*control center*/
        mutableListOf("gnome-control-center","start control","open \"x-apple.systempreferences:com.apple.preference.security\"")
    )


    fun setup(IP_Address: String, port: Int) {
        this.port = port
        this.IP_address = IP_Address
    }

    fun sendCommand(
        command: String
    ) {
        /*
        * sending commands from phone to pc
        * */
        try {
            dataOutputStream!!.writeUTF(command)
            Log.d("sendcommand","done $command")
        } catch (er: Exception) {
            println(er)
        }

    }

    fun socketConnecting(): Boolean {
        /*
        * connect to socket server
        * initialize output and input streams
        * returns true if connected
        * returns false if failed and log
        * */
        return try {
            socket = Socket(IP_address, port)
            dataOutputStream = DataOutputStream(socket?.getOutputStream())
            dataInputStream = DataInputStream(socket?.getInputStream())
            true
        } catch (er: Exception) {
            socket = null
            Log.e("fun socketConnecting", "failed : ${er}")
            false
        }
    }

    fun cutSocket() {
        try {
            socket!!.close()
            socket = null
            dataInputStream = null
            dataOutputStream = null
        } catch (_: Exception) {
        }

    }

    fun authentication(password: Int): Int {
        /*
        * returns 0 if failed
        * returns 1 if succeed
        * else number maybe error
        * */
        try {
            dataOutputStream!!.writeInt(password)
            return dataInputStream!!.readInt()
        } catch (_: Exception) {
            return 0
        }
    }
}