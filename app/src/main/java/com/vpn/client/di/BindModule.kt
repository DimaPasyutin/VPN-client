package com.vpn.client.di

import com.vpn.client.data.InternetConnection
import com.vpn.client.data.VPN
import com.vpn.client.domain.gateways.Connection
import com.vpn.client.domain.gateways.VpnController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface BindModule {

    @Binds
    fun bindsConnection(connection: InternetConnection): Connection

    @Binds
    fun bindsVpnController(vpn: VPN): VpnController

}