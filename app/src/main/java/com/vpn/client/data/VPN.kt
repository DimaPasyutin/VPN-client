package com.vpn.client.data

import android.app.Application
import android.content.Intent
import android.net.VpnService
import android.os.RemoteException
import android.util.Log
import com.vpn.client.models.Server
import de.blinkt.openvpn.OpenVpnApi
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.OpenVPNThread
import de.blinkt.openvpn.core.VpnStatus
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

class VPN @Inject constructor(
    private val context: Application,
    private var vpnService: OpenVPNService,
    private var openVPNThread: OpenVPNThread,
    private val connection: InternetConnection,
) {

    init {
        VpnStatus.initLogCache(context.cacheDir)
    }

    fun prepareVpn(): Intent? = VpnService.prepare(context)

    fun stopVpn() = openVPNThread.stopProcess()

    fun vpnStarted() = vpnService.isConnected

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
            Log.e("!!!", config.toString())
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
