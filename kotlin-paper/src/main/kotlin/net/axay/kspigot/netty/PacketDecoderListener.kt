package net.axay.kspigot.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import net.minecraft.network.protocol.Packet
import org.bukkit.entity.Player

class PacketDecoderListener(
    val player: Player
) : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any?) {
        if (msg !is Packet<*>) {
            super.channelRead(ctx, msg)
            return
        }

        val clazz = msg.javaClass
        val listeners = PacketHandler.customPacketListeners[clazz]
        if (listeners == null) {
            super.channelRead(ctx, msg)
            return
        }

        val event = PacketEvent(msg, player, ctx)
        listeners.forEach { it(event) }
        if (event.cancelled)
            return

        super.channelRead(ctx, msg)
    }
}

