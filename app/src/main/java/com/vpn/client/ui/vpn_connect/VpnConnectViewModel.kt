package com.vpn.client.ui.vpn_connect

import androidx.lifecycle.viewModelScope
import com.vpn.client.R
import com.vpn.client.utils.ToastShower
import com.vpn.client.domain.use_cases.VpnConnectUseCase
import com.vpn.client.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VpnConnectViewModel @Inject constructor(
    private val toast: ToastShower,
    private val useCase: VpnConnectUseCase,
) : BaseViewModel<VpnConnectScreenState>(VpnConnectScreenState()) {

    init {
        initVpnSpeedObserver()
    }

    fun obtainEvent(event: VpnConnectEvent) {
        when (event) {
            is VpnConnectEvent.Connect -> requestConnectionVpn()
            is VpnConnectEvent.PermissionDenied -> setEvent(event)
            is VpnConnectEvent.StartVpn -> startVpn()
            is VpnConnectEvent.StopVpn -> useCase.stopVpn()
            is VpnConnectEvent.RequestPermission -> {}
            is VpnConnectEvent.Default -> {}
        }
        setEvent(event)
    }

    private fun initVpnSpeedObserver() {
        viewModelScope.launch(Dispatchers.Default) {
            useCase.getVpnStatus().collect { speed ->
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
        useCase.startVpn(screenState.value.server)
    }

    private fun requestConnectionVpn() {
        if (useCase.hasConnection()) {
            connectToVpnOrRequestPermission()
        } else {
            toast.show(R.string.connect_to_internet)
            setEvent(VpnConnectEvent.Default)
        }
    }

    private fun connectToVpnOrRequestPermission() {
        val intent = useCase.prepareVpn()
        if (!screenState.value.isVpnStarted && intent != null) {
            setEvent(VpnConnectEvent.RequestPermission(intent))
        } else {
            useCase.startVpn(screenState.value.server)
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
