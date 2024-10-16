package net.axay.kspigot.extensions.bukkit

import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin

/**
 * Registers this CommandExecutor for
 * the given command.
 * @return If the command was registered successfully.
 */
fun CommandExecutor.register(plugin: JavaPlugin, commandName: String): Boolean {
    plugin.getCommand(commandName)?.let {
        it.setExecutor(this)
        if (this is TabCompleter)
            it.tabCompleter = this
        return true
    }
    return false
}
