package net.axay.kspigot.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

abstract class CustomEvent(async: Boolean = false) : Event(async) {
    companion object {
        @JvmStatic
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }
}
