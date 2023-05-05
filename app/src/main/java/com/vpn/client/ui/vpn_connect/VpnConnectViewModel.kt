package com.vpn.client.ui.vpn_connect

import androidx.lifecycle.viewModelScope
import com.vpn.client.R
import com.vpn.client.data.InternetConnection
import com.vpn.client.utils.ToastShower
import com.vpn.client.data.VPN
import com.vpn.client.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VpnConnectViewModel @Inject constructor(
    private val vpn: VPN,
    private val toast: ToastShower,
    private val connection: InternetConnection,
) : BaseViewModel<VpnConnectScreenState>(VpnConnectScreenState()) {

    init {
        initVpnSpeedObserver()
    }

    fun obtainEvent(event: VpnConnectEvent) {
        when (event) {
            is VpnConnectEvent.Connect -> requestConnectionVpn()
            is VpnConnectEvent.PermissionDenied -> setEvent(event)
            is VpnConnectEvent.StartVpn -> startVpn()
            is VpnConnectEvent.StopVpn -> vpn.stopVpn()
            is VpnConnectEvent.RequestPermission -> {}
            is VpnConnectEvent.Default -> {}
        }
        setEvent(event)
    }

    private fun initVpnSpeedObserver() {
        viewModelScope.launch(Dispatchers.Default) {
            vpn.vpnStatus.collect { speed ->
                updateState {
                    it.copy(
                        connectionSpeed = speed,
                        isVpnStarted = speed.byteIn.isNotEmpty() && speed.byteOut.isNotEmpty()
                    )
                }
            }
        }
    }

    private fun startVpn() {
        vpn.startVpn(screenState.value.server)
    }

    private fun requestConnectionVpn() {
        if (connection.hasConnection()) {
            connectToVpnOrRequestPermission()
        } else {
            toast.show(R.string.connect_to_internet)
            setEvent(VpnConnectEvent.Default)
        }
    }

    private fun connectToVpnOrRequestPermission() {
        val intent = vpn.prepareVpn()
        if (vpn.vpnStatus.value.byteIn.isEmpty() && vpn.vpnStatus.value.byteOut.isEmpty() && intent != null) {
            setEvent(VpnConnectEvent.RequestPermission(intent))
        } else {
            vpn.startVpn(screenState.value.server)
            setEvent(VpnConnectEvent.Default)
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
