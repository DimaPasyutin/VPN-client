package com.vpn.client.ui.vpn_connect

import android.os.Bundle
import android.view.View
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserverStateScreen()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.obtainEvent(VpnConnectEvent.Default)
    }

    private fun initObserverStateScreen() {
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: VpnConnectScreenState) {
        with(binding) {
            val textConnected = if (state.isConnected) R.string.switch_on else R.string.switch_off
            connectStatus.text = getText(textConnected)
        }
    }

    private fun initListeners() {
        binding.btnConnect.setOnClickListener {
            viewModel.obtainEvent(VpnConnectEvent.Connect)
        }
    }
}
