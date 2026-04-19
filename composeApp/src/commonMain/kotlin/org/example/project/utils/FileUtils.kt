package org.example.project.utils

data class DeviceInfo(val id: String, val location: String)
expect suspend fun pickDirectory(): DeviceInfo?