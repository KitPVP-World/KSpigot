package net.axay.kspigot.netty

import net.minecraft.network.protocol.Packet

/**
 * Packet that is not intended to be processed by plugins
 */
class ProtectedPacket(
    val originalPacket: Packet<*>
)