package net.axay.kspigot.netty

import io.netty.channel.ChannelHandlerContext
import net.minecraft.network.protocol.Packet
import org.bukkit.entity.Player

/**
 * An event that listens for packets sent to the [player]
 */
class PacketEvent<T : Packet<*>>(
    val packet: T,
    val player: Player,
    val ctx: ChannelHandlerContext,
) {

    var cancelled = false
}
