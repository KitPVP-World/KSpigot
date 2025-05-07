package net.axay.kspigot.netty

import net.axay.kspigot.event.listen
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBundlePacket
import org.bukkit.Server
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object PacketHandler {

    internal val listeners = ConcurrentHashMap<UUID, PacketListener>()
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
        val listener = PacketListener(player)

        val channel = player.nms.connection.connection.channel
        channel.pipeline()
            .addLast("core_packets", listener)
        this.listeners[player.uniqueId] = listener
    }

    internal fun uninject(player: Player) {
        val channel = player.nms.connection.connection.channel
        channel.pipeline()
            .remove("core_packets")
        this.listeners.remove(player.uniqueId)
    }

    fun <T : Packet<ClientGamePacketListener>> register(packetClass: Class<T>, block: (PacketEvent<T>) -> Unit) {
        this.customPacketListeners.getOrPut(packetClass) { HashSet() }
            .add { event -> block(event as PacketEvent<T>) }
    }
}

/**
 * Creates a listener for a specific packet.
 * The function gets called when the packet is sent to the player's connection
 * @param block function to get executed
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Packet<ClientGamePacketListener>> packetEvent(noinline block: (PacketEvent<T>) -> Unit) =
    PacketHandler.register(T::class.java, block)

/**
 * Sending a [ProtectedPacket] to the player, not being processed by other plugins
 * @param packet packet to send
 */
fun Player.sendPacketSilently(packet: Packet<ClientGamePacketListener>) {
    val pipeline = (this as CraftPlayer).handle.connection.connection.channel.pipeline()
    pipeline.write(ProtectedPacket(packet))
}

/**
 * Sending a [ProtectedPacket] that wraps a [ClientboundBundlePacket] to the player,
 * not being processed by other plugins
 * @param packets packet to send
 */
fun Player.sendPacketsSilently(vararg packets: Packet<ClientGamePacketListener>) {
    sendPacketSilently(ClientboundBundlePacket(packets.toSet()))
}
