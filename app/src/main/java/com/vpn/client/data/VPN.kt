package com.vpn.client.data

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.VpnService
import android.os.RemoteException
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vpn.client.models.ConnectionSpeed
import com.vpn.client.models.Server
import de.blinkt.openvpn.OpenVpnApi
import de.blinkt.openvpn.core.OpenVPNThread
import de.blinkt.openvpn.core.VpnStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

class VPN @Inject constructor(
    private val context: Application,
    private var openVPNThread: OpenVPNThread,
) {

    private val _vpnStatus = MutableStateFlow(ConnectionSpeed())
    val vpnStatus = _vpnStatus.asStateFlow()

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                var byteIn = intent.getStringExtra("byteIn")
                val byteOut = intent.getStringExtra("byteOut")
                _vpnStatus.value = ConnectionSpeed(byteIn ?: "", byteOut ?: "")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    init {
        VpnStatus.initLogCache(context.cacheDir)
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, IntentFilter("connectionState"))
    }

    fun prepareVpn(): Intent? = VpnService.prepare(context)

    fun stopVpn() = openVPNThread.stopProcess()

    fun startVpn(server: Server) {
        try {
            val conf: InputStream = context.assets.open(server.ovpn)
            val isr = InputStreamReader(conf)
            val br = BufferedReader(isr)
            val config = StringBuilder()
            var line: String?
            while (true) {
                line = br.readLine()
                if (line == null) break
                config.append(line).append("\n")
            }
            br.readLine()
            OpenVpnApi.startVpn(
                context,
                config.toString(),
                server.country,
                server.ovpnUserName,
                server.ovpnUserPassword
            )
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }
}
