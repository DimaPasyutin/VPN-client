package com.vpn.client.ui.vpn_connect

import com.vpn.client.R
import com.vpn.client.data.InternetConnection
import com.vpn.client.data.ToastShower
import com.vpn.client.data.VPN
import com.vpn.client.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VpnConnectViewModel @Inject constructor(
    private val vpn: VPN,
    private val toast: ToastShower,
    private val connection: InternetConnection,
) : BaseViewModel<VpnConnectScreenState>(VpnConnectScreenState()) {

    fun obtainEvent(event: VpnConnectEvent) {
        when (event) {
            is VpnConnectEvent.Default -> updateState()
            is VpnConnectEvent.Connect -> connectToVpn()
            is VpnConnectEvent.PermissionDenied -> setEvent(event)
            is VpnConnectEvent.StartVpn -> startVpn()
            is VpnConnectEvent.StopVpn -> vpn.stopVpn()
            is VpnConnectEvent.RequestPermission -> {}
        }
        setEvent(event)
    }

    private fun startVpn() {
        vpn.startVpn(screenState.value.server)
        updateState()
    }

    private fun updateState() {
        updateState {
            it.copy(
                isConnected = vpn.vpnStarted()
            )
        }
    }

    private fun connectToVpn() {
        if (connection.hasConnection()) {
            val intent = vpn.prepareVpn()
            if (!vpn.vpnStarted() && intent != null) {
                setEvent(VpnConnectEvent.RequestPermission(intent))
            } else {
                vpn.startVpn(screenState.value.server)
            }
        } else {
            toast.show(R.string.connect_to_internet)
        }
    }

    private fun setEvent(event: VpnConnectEvent) {
        updateState {
            it.copy(
                event = event
            )
        }
    }
}
