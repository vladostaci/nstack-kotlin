package dk.nodes.nstack.kotlin.managers

import android.content.Context
import android.net.ConnectivityManager

class ConnectionManager(private val context: Context) {
    fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnectedOrConnecting
    }
}