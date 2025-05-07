package net.axay.kspigot.netty

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import net.minecraft.network.protocol.Packet
import org.bukkit.entity.Player

class PacketListener(
    val player: Player
) : ChannelDuplexHandler() {

    override fun write(ctx: ChannelHandlerContext, msg: Any?, promise: ChannelPromise?) {
        if (msg is ProtectedPacket) {
            val packet = msg.originalPacket
            super.write(ctx, packet, promise)
            return
        }

        if (msg is Packet<*>) {
            val clazz = msg.javaClass
            val listeners = PacketHandler.customPacketListeners[clazz] ?: return
            val event = PacketEvent(msg, player, ctx)
            listeners.forEach { it(event) }
            if (event.cancelled) return
            super.write(ctx, msg, promise)
            return
        }
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any?) {
        if (msg is ProtectedPacket) {
            val packet = msg.originalPacket
            super.channelRead(ctx, packet)
            return
        }
        if (msg is Packet<*>) {
            val clazz = msg.javaClass
            val listeners = PacketHandler.customPacketListeners[clazz] ?: return
            val event = PacketEvent(msg, player, ctx)
            listeners.forEach { it(event) }
            if (event.cancelled) return
            super.channelRead(ctx, msg)
        }
    }

}
