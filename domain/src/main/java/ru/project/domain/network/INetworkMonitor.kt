package ru.project.domain.network

interface INetworkMonitor {
    fun isConnected(): Boolean
}