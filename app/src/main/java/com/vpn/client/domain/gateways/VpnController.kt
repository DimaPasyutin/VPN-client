package com.vpn.client.domain.gateways

import android.content.Intent
import com.vpn.client.domain.models.ConnectionSpeed
import com.vpn.client.domain.models.Server
import kotlinx.coroutines.flow.StateFlow

interface VpnController {

    val vpnStatus: StateFlow<ConnectionSpeed>

    fun prepareVpn(): Intent?

    fun stopVpn()

    fun startVpn(server: Server)

}
