package world.kitpvp.kotlin

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.ProxyServer
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIVelocityConfig
import org.slf4j.Logger

class KotlinVelocityPlugin @Inject constructor(server: ProxyServer, logger: Logger) {
    init {
        val config = CommandAPIVelocityConfig(server, this)
            .setNamespace("kitpvp")
        if (System.getProperty("core.commandapi.debug") != null)
            config
                .verboseOutput(true)
                .silentLogs(false)

        CommandAPI.onLoad(config)
    }

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        CommandAPI.onEnable()
    }
}