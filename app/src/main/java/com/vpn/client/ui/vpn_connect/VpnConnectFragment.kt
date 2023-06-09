package com.vpn.client.ui.vpn_connect

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vpn.client.R
import com.vpn.client.databinding.FragmentVpnConnectBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VpnConnectFragment : Fragment(R.layout.fragment_vpn_connect) {

    private val binding: FragmentVpnConnectBinding by viewBinding()

    private val viewModel: VpnConnectViewModel by viewModels()

    private val startActivityForVpnPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                viewModel.obtainEvent(VpnConnectEvent.StartVpn)
            } else {
                viewModel.obtainEvent(VpnConnectEvent.PermissionDenied)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserverStateScreen()
        initListeners()
    }

    private fun initObserverStateScreen() {
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                renderState(state)
                requestPermission(state.event)
            }
        }
    }

    private fun renderState(state: VpnConnectScreenState) {
        with(binding) {
            val textConnected = if (state.isVpnStarted) R.string.switch_on else R.string.switch_off
            val vpnStatus = if (state.isVpnStarted) R.string.disconnect else R.string.connect
            connectStatus.text = getText(textConnected)
            btnConnect.text = getText(vpnStatus)
        }
    }

    private fun initListeners() {
        binding.btnConnect.setOnClickListener {
            if (viewModel.screenState.value.isVpnStarted) {
                viewModel.obtainEvent(VpnConnectEvent.StopVpn)
            } else {
                viewModel.obtainEvent(VpnConnectEvent.Connect)
            }
        }
    }

    private fun requestPermission(event: VpnConnectEvent) {
        if (event !is VpnConnectEvent.RequestPermission) return
        startActivityForVpnPermission.launch(event.intent)
    }
}
