package net.axay.kspigot.event

import net.axay.kspigot.extensions.pluginManager
import net.axay.kspigot.main.KSpigot
import net.axay.kspigot.plugin.KSpigotPluginManager
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

/**
 * Shortcut for unregistering all events in this listener.
 */
fun Listener.unregister() = HandlerList.unregisterAll(this)

inline fun <reified T: Event> Listener.register(
    @Suppress("UNUSED_PARAMETER") plugin: KSpigot,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    noinline executor: (Listener, Event) -> Unit,
) = register<T>(priority, ignoreCancelled, executor)

/**
 * Registers the event with a custom event [executor].
 *
 * @param T the type of event
 * @param priority the priority when multiple listeners handle this event
 * @param ignoreCancelled if manual cancellation should be ignored
 * @param executor handles incoming events
 */
inline fun <reified T : Event> Listener.register(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    noinline executor: (Listener, Event) -> Unit,
) {
    pluginManager.registerEvent(T::class.java, this, priority, executor, KSpigotPluginManager, ignoreCancelled)
}

/**
 * This class represents a [Listener] with
 * only one event to listen to.
 */
abstract class SingleListener<T : Event>(
    val priority: EventPriority,
    val ignoreCancelled: Boolean,
) : Listener {
    abstract fun onEvent(event: T)
}


/**
 * Registers the [SingleListener] with its
 * event listener.
 */
inline fun <reified T: Event> SingleListener<T>.register() {
    pluginManager.registerEvent(
        T::class.java,
        this,
        priority,
        { _, event -> (event as? T)?.let { this.onEvent(it) } },
        KSpigotPluginManager,
        ignoreCancelled
    )
}

/**
 * Registers the listener automatically
 * @param T the type of event to listen to
 * @param priority the priority when multiple listeners handle this event
 * @param ignoreCancelled if manual cancellation should be ignored
 * @param onEvent the event callback
 */
@JvmOverloads // Keep legacy code working
inline fun <reified T: Event> listen(
    register: Boolean = true,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline onEvent: (event: T) -> Unit,
): SingleListener<T> {
    val listener = object: SingleListener<T>(priority, ignoreCancelled) {
        override fun onEvent(event: T) = onEvent.invoke(event)
    }
    if(register)
        listener.register()
    return listener
}