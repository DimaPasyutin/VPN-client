package com.vpn.client.ui.vpn_connect

import com.vpn.client.models.Server

data class VpnConnectScreenState(
    val server: Server = Server(),
    val isConnected: Boolean = false,
    val event: VpnConnectEvent = VpnConnectEvent.Default
)

sealed class VpnConnectEvent {
    object Default : VpnConnectEvent()
    object Connect : VpnConnectEvent()
}
