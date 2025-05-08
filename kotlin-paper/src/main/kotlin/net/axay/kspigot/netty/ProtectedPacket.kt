package net.axay.kspigot.netty

import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.PacketType
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.resources.ResourceLocation

/**
 * Packet that is not intended to be processed by plugins
 */
class ProtectedPacket(
    val originalPacket: Packet<*>
): Packet<ClientGamePacketListener> {

    companion object {
        val CLIENTBOUND_PROTECTED_PACKET: PacketType<ProtectedPacket> =
            PacketType<ProtectedPacket>(PacketFlow.CLIENTBOUND, ResourceLocation.fromNamespaceAndPath("core", "protected_packet"))
    }

    override fun type() = CLIENTBOUND_PROTECTED_PACKET
    override fun handle(listener: ClientGamePacketListener) {
        // no handle -> on server
    }
}