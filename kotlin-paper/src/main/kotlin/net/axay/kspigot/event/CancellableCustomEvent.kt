package net.axay.kspigot.event

import org.bukkit.event.Cancellable

abstract class CancellableCustomEvent(async: Boolean = false) : CustomEvent(async), Cancellable {

    private var cancelled = false

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }

}
