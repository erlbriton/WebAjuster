package org.example.project

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")} on Linux"
}

actual fun getPlatform(): Platform = JVMPlatform()