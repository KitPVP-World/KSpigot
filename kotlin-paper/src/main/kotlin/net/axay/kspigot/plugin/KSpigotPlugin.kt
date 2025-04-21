package net.axay.kspigot.plugin

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

class KSpigotPlugin: JavaPlugin() {

    override fun onLoad() {
        val config = CommandAPIBukkitConfig(this)
            .setNamespace("kitpvp")
            .shouldHookPaperReload(true)

        if(System.getProperty("core.commandapi.debug") != null)
            config
                .verboseOutput(true)
                .silentLogs(false)

        CommandAPI.onLoad(config)
    }

    override fun onEnable() {
        CommandAPI.onEnable()
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }

    companion object {
        internal lateinit var instance: KSpigotPlugin
            private set
    }

    init {
        instance = this
    }
}

@delegate:ApiStatus.Internal
val KSpigotPluginManager by lazy { KSpigotPlugin.instance }