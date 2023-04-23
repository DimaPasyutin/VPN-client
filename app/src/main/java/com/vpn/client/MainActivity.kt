package com.vpn.client

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vpn.client.ui.vpn_connect.VpnConnectFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.main_graph, VpnConnectFragment(), null)
            .addToBackStack(null).commit()
    }
}
