package net.axay.kspigot.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import net.minecraft.network.protocol.Packet
import org.bukkit.entity.Player
import kotlin.collections.plusAssign

class PacketEncoderListener(private val player: Player): MessageToMessageEncoder<Any>() {
    override fun encode(
        ctx: ChannelHandlerContext,
        msg: Any,
        out: MutableList<Any>
    ) {
        if(msg !is Packet<*>) {
            out += msg
            return
        }

        if (msg is ProtectedPacket) {
            val packet = msg.originalPacket
            out += packet
            return
        }

        val clazz = msg.javaClass
        val listeners = PacketHandler.customPacketListeners[clazz]
        if(listeners == null)  {
            out += msg
            return
        }

        val event = PacketEvent(msg, player, ctx)
        listeners.forEach { it(event) }
        if (event.cancelled)
            return
        out += msg
    }
}