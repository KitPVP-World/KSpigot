package net.axay.kspigot.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundBundlePacket
import org.bukkit.entity.Player

class PacketEncoderListener(private val player: Player) : ChannelOutboundHandlerAdapter() {
    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        if (msg !is Packet<*>) {
            super.write(ctx, msg, promise)
            return
        }

        if (msg is ProtectedPacket) {
            val packet = msg.originalPacket
            if (packet is ClientboundBundlePacket) {
                for (subPacket in packet.subPackets()) {
                    super.write(ctx, subPacket, promise)
                }
            } else {
                super.write(ctx, packet, promise)
            }
            return
        }

        val clazz = msg.javaClass
        val listeners = PacketHandler.customPacketListeners[clazz]
        if (listeners == null) {
            super.write(ctx, msg, promise)
            return
        }
        val event = PacketEvent(msg, player, ctx)
        listeners.forEach { it(event) }
        if (event.cancelled)
            return
        super.write(ctx, event.packet, promise)
    }
}