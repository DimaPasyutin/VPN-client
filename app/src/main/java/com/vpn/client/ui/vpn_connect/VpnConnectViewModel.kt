package com.vpn.client.ui.vpn_connect

import com.vpn.client.R
import com.vpn.client.data.InternetConnection
import com.vpn.client.data.ToastShower
import com.vpn.client.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VpnConnectViewModel @Inject constructor(
    private val toast: ToastShower,
    private val connection: InternetConnection,
) : BaseViewModel<VpnConnectScreenState>(VpnConnectScreenState()) {

    fun obtainEvent(event: VpnConnectEvent) {
        when (event) {
            VpnConnectEvent.Default -> updateState()
            VpnConnectEvent.Connect -> connectToVpn()
        }
        setEvent(event)
    }

    private fun updateState() {
        updateState {
            it.copy()
        }
    }

    private fun connectToVpn() {
        if (connection.hasConnection()) {
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
