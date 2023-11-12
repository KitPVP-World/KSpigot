package net.axay.kspigot.event

import net.axay.kspigot.extensions.pluginManager
import net.axay.kspigot.main.KSpigot
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

/**
 * Shortcut for unregistering all events in this listener.
 */
fun Listener.unregister() = HandlerList.unregisterAll(this)

/**
 * Registers the event with a custom event [executor].
 *
 * @param T the type of event
 * @param priority the priority when multiple listeners handle this event
 * @param ignoreCancelled if manual cancellation should be ignored
 * @param executor handles incoming events
 */
inline fun <reified T : Event> Listener.register(
    plugin: KSpigot,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    noinline executor: (Listener, Event) -> Unit,
) {
    pluginManager.registerEvent(T::class.java, this, priority, executor, plugin, ignoreCancelled)
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
inline fun <reified T : Event> SingleListener<T>.register(plugin: Plugin) {
    pluginManager.registerEvent(
        T::class.java,
        this,
        priority,
        { _, event -> (event as? T)?.let { this.onEvent(it) } },
        plugin,
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
inline fun <reified T : Event> listen(
    plugin: Plugin,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline onEvent: (event: T) -> Unit,
): SingleListener<T> {
    val listener = object : SingleListener<T>(priority, ignoreCancelled) {
        override fun onEvent(event: T) = onEvent.invoke(event)
    }
    listener.register(plugin)
    return listener
}

/**
 * Manual registration needed!
 * @see SingleListener.register(plugin)
 * @param T the type of event to listen to
 * @param priority the priority when multiple listeners handle this event
 * @param ignoreCancelled if manual cancellation should be ignored
 * @param onEvent the event callback
 */
inline fun <reified T: Event> listen(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline onEvent: (event: T) -> Unit,
): SingleListener<T> {
    return object: SingleListener<T>(priority, ignoreCancelled) {
        override fun onEvent(event: T) = onEvent.invoke(event)
    }
}
