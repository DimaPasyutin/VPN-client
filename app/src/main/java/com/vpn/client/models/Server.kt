package com.vpn.client.models

data class Server(
    private val ovpn: String = "netherlands.ovpn",
    private val ovpnUserName: String = "dima",
    private val ovpnUserPassword: String = "1029384756",
)
