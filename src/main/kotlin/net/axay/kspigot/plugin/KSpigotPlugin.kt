package net.axay.kspigot.plugin

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import net.axay.kspigot.commands.internal.BrigardierSupport
import net.axay.kspigot.languageextensions.kotlinextensions.closeIfInitialized
import net.axay.kspigot.runnables.KRunnableHolder
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

class KSpigotPlugin: JavaPlugin() {

    // lazy properties
    private val kRunnableHolderProperty = lazy { KRunnableHolder }
    internal val kRunnableHolder by kRunnableHolderProperty

    private val brigardierSupportProperty = lazy { BrigardierSupport(this) }
    val brigardierSupport by brigardierSupportProperty



    override fun onLoad() {
        CommandAPI.onLoad(
            CommandAPIBukkitConfig(this)
                .silentLogs(true)
                .shouldHookPaperReload(true)
        )
    }

    override fun onEnable() {
        CommandAPI.onEnable()

        // only register the commands if the plugin has not disabled itself
        if(this.isEnabled) {
            brigardierSupport.registerAll()
        }
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