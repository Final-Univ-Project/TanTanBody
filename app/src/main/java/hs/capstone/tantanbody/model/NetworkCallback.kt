package hs.capstone.tantanbody.model

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import hs.capstone.tantanbody.ui.BlockingActivity
import hs.capstone.tantanbody.ui.LogInActivity

class NetworkCallback(val ctx: Context): ConnectivityManager.NetworkCallback() {
    lateinit var nwRequest: NetworkRequest
    lateinit var connectManager: ConnectivityManager

    init {
        nwRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        connectManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectManager.registerNetworkCallback(nwRequest, this)
    }
    fun disableTracking() {
        connectManager.unregisterNetworkCallback(this)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)

        // 다시 연결되면
        val intent = Intent(ctx, LogInActivity::class.java)
        ctx.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))
    }
    override fun onLost(network: Network) {
        super.onLost(network)

        // 인터넷 연결 안될 때
        val intent = Intent(ctx, BlockingActivity::class.java)
        ctx.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))
    }
}