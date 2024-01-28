package net.axay.kspigot.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.axay.kspigot.annotations.NMS_General
import net.axay.kspigot.plugin.KSpigotPluginManager
import net.minecraft.commands.CommandSourceStack

/**
 * Registers this command at the [CommandDispatcher] of the server.
 *
 * @param sendToPlayers whether the new command tree should be send to
 * all players, this is true by default, but you can disable it if you are
 * calling this function as the server is starting
 */
@NMS_General
fun LiteralArgumentBuilder<CommandSourceStack>.register(sendToPlayers: Boolean = true) {
    val brigardierSupport = KSpigotPluginManager.brigardierSupport
    if (!brigardierSupport.executedDefaultRegistration)
        brigardierSupport.commands += this
    else {
        brigardierSupport.resolveCommandManager().dispatcher.register(this)
        if (sendToPlayers)
            brigardierSupport.updateCommandTree()
    }
}
