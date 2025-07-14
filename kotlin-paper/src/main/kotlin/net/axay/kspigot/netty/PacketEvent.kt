package net.axay.kspigot.netty

import io.netty.channel.ChannelHandlerContext
import net.minecraft.network.protocol.Packet
import org.bukkit.entity.Player

/**
 * An event that listens for packets sent to the [player]
 */
data class PacketEvent<T : Packet<*>>(
    var packet: T,
    val player: Player,
    val ctx: ChannelHandlerContext,
) {

    var cancelled = false
}
