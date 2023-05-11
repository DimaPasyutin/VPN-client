package com.vpn.client.domain.use_cases

import android.content.Intent
import com.vpn.client.domain.gateways.Connection
import com.vpn.client.domain.gateways.VpnController
import com.vpn.client.domain.models.ConnectionSpeed
import com.vpn.client.domain.models.Server
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VpnConnectUseCase @Inject constructor(
    private val connection: Connection,
    private val vpnController: VpnController,
) {

    fun stopVpn() = vpnController.stopVpn()

    fun startVpn(server: Server) = vpnController.startVpn(server)

    fun prepareVpn(): Intent? = vpnController.prepareVpn()

    fun getVpnStatus(): Flow<ConnectionSpeed> = vpnController.vpnStatus

    fun hasConnection(): Boolean = connection.hasConnection()

}
