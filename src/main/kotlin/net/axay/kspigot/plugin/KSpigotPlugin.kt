package net.axay.kspigot.plugin

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import net.axay.kspigot.languageextensions.kotlinextensions.closeIfInitialized
import net.axay.kspigot.runnables.KRunnableHolder
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

class KSpigotPlugin: JavaPlugin() {

    // lazy properties
    private val kRunnableHolderProperty = lazy { KRunnableHolder }
    internal val kRunnableHolder by kRunnableHolderProperty

    override fun onLoad() {
        CommandAPI.onLoad(
            CommandAPIBukkitConfig(this)
                .silentLogs(false)
                .verboseOutput(true)
                .shouldHookPaperReload(true)
        )
    }

    override fun onEnable() {
        CommandAPI.onEnable()
    }

    override fun onDisable() {
        CommandAPI.onDisable()

        // avoid unnecessary load of lazy properties
        kRunnableHolderProperty.closeIfInitialized()
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