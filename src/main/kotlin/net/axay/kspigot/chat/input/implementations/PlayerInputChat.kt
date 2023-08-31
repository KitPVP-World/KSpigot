package net.axay.kspigot.chat.input.implementations

import io.papermc.paper.event.player.AsyncChatEvent
import net.axay.kspigot.chat.input.PlayerInput
import net.axay.kspigot.chat.input.PlayerInputResult
import net.axay.kspigot.event.listen
import net.axay.kspigot.main.KSpigot
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority

internal class PlayerInputChat(
    plugin: KSpigot,
    player: Player,
    callback: (PlayerInputResult<Component>) -> Unit,
    timeoutSeconds: Int,
    question: Component,
) : PlayerInput<Component>(plugin, player, callback, timeoutSeconds) {
    init {
        player.sendMessage(question)
    }

    override val inputListeners = listOf(
        listen<AsyncChatEvent>(plugin, EventPriority.LOWEST) {
            if (it.player == player) {
                it.isCancelled = true
                onReceive(it.message())
            }
        }
    )
}
