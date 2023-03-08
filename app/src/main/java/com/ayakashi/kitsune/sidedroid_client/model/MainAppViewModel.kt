package com.ayakashi.kitsune.sidedroid_client.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ayakashi.kitsune.sidedroid_client.data.IPAddressDataModel
import com.ayakashi.kitsune.sidedroid_client.data.IPAddressItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainAppViewModel : ViewModel() {
    private val dataModelState = MutableStateFlow(IPAddressDataModel())
    val uiDataModelState: StateFlow<IPAddressDataModel> = dataModelState.asStateFlow()

    private val networkSearchScope = CoroutineScope(Dispatchers.IO)

    /*  Store and get what local ip addresses it detects    */
    suspend fun addIp() : Boolean{
        dataModelState.update {
            IPAddressDataModel()
        }
        try {
            val list = mutableListOf<IPAddressItem>()
            val tag = "VIEW-MODEL"
            /*  multithreading search */
            /* Thread 1*/
            val thread = withContext(networkSearchScope.coroutineContext) {
                async {
                    searchIP(list, 1)
                }
                async {
                    searchIP(list, 85)
                }
                async {
                    searchIP(list, 170)
                    Log.d(tag, "done scanning")
                }
            }

            if(thread.isCompleted){
                Log.d("MainAppViewModel","storing")
                dataModelState.update {
                    it.copy(
                        IPAddressItemList = list
                    )
                }
            }
            Log.d("MainAppViewModel","returning")
            return false
        }
        catch (error: Exception){
            return true;
        }
    }

    /*  Prints the value of the array from the DataModel*/
    fun printList(){
        dataModelState.value.IPAddressItemList.forEach{
            println(it.ip)
        }
    }
}