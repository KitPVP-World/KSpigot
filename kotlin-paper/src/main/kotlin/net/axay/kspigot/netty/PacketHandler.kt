package net.axay.kspigot.netty

import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.console
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBundlePacket
import org.bukkit.Server
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PacketHandler {
    internal val customPacketListeners = HashMap<Class<out Packet<*>>, HashSet<(PacketEvent<*>) -> Unit>>()

    private val Player.nms get() = (this as CraftPlayer).handle

    internal fun register() {
        listen<PlayerJoinEvent> {
            inject(it.player)
        }

        listen<PlayerQuitEvent> {
            uninject(it.player)
        }
    }

    internal fun unregister(server: Server) {
        for (player in server.onlinePlayers) {
            uninject(player)
        }
    }

    internal fun inject(player: Player) {
        val channel = player.nms.connection.connection.channel

        channel.pipeline()
            .addAfter("encoder", "kotlin-paper-encoder", PacketEncoderListener(player))
            .addAfter("decoder", "kotlin-paper-decoder", PacketDecoderListener(player))

        console.sendRichMessage(channel.pipeline().names().joinToString("\n") { " - $it: ${channel.pipeline()[it]}" })
    }

    internal fun uninject(player: Player) {
        val channel = player.nms.connection.connection.channel
        if(channel.pipeline().get("kotlin-paper-encoder") != null)
            channel.pipeline().remove("kotlin-paper-encoder")
        if(channel.pipeline().get("kotlin-paper-decoder") != null)
            channel.pipeline().remove("kotlin-paper-decoder")
    }

    fun <T : Packet<*>> register(packetClass: Class<T>, block: (PacketEvent<T>) -> Unit) {
        this.customPacketListeners.getOrPut(packetClass) { HashSet() }
            .add { event -> block(event as PacketEvent<T>) }
    }
}

/**
 * Creates a listener for a specific packet.
 * The function gets called when the packet is sent/or received from the player's connection
 * For sent packets see [ClientGamePacketListener]
 * For received packets see [net.minecraft.network.protocol.game.ServerGamePacketListener]
 *
 * @param block function to get executed
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Packet<*>> packetEvent(noinline block: (PacketEvent<T>) -> Unit) =
    PacketHandler.register(T::class.java, block)

/**
 * Sending a [ProtectedPacket] to the player, not being processed by other plugins
 * @param packet packet to send
 */
fun Player.sendPacketSilently(packet: Packet<ClientGamePacketListener>) {
    val connection = (this as CraftPlayer).handle.connection
    connection.send(ProtectedPacket(packet))
}

/**
 * Sending a [ProtectedPacket] that wraps a [ClientboundBundlePacket] to the player,
 * not being processed by other plugins
 * @param packets packet to send
 */
fun Player.sendPacketsSilently(vararg packets: Packet<ClientGamePacketListener>) {
    sendPacketSilently(ClientboundBundlePacket(packets.toSet()))
}
