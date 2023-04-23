package com.vpn.client.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.OpenVPNThread

@Module
@InstallIn(SingletonComponent::class)
class ModuleProvider {

    @Provides
    fun provideOpenVPNService(): OpenVPNService = OpenVPNService()

    @Provides
    fun provideOpenVPNThread(): OpenVPNThread = OpenVPNThread()

}
