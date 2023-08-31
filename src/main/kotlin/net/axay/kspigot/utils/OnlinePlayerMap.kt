@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.kspigot.utils

import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.util.*

class PlayerMapHolder(plugin: Plugin) {
    internal val maps = HashSet<OnlinePlayerMap<*>>()

    init {
        listen<PlayerQuitEvent>(plugin) { event ->
            maps.removeIf {
                if (it.internalMap.remove(event.player.uniqueId) != null) {
                    it.internalMap.isEmpty()
                } else false
            }
        }
    }
}

/**
 * A map where entries will be removed
 * atomatically when a player leaves the server.
 */
class OnlinePlayerMap<V>(val playerMapHolder: PlayerMapHolder) {
    val internalMap = HashMap<UUID, V>()

    operator fun get(player: Player) = internalMap[player.uniqueId]

    operator fun set(player: Player, value: V) {
        if (internalMap.isEmpty())
            playerMapHolder.maps += this
        internalMap[player.uniqueId] = value
    }

    operator fun minusAssign(player: Player) {
        internalMap -= player.uniqueId
        if (internalMap.isEmpty())
            playerMapHolder.maps -= this
    }
}
