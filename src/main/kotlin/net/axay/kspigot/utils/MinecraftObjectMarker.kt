package net.axay.kspigot.utils

import net.axay.kspigot.extensions.pluginKey
import net.axay.kspigot.items.meta
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

private fun markerKey(plugin: Plugin, key: String) = pluginKey(plugin, "kspigot_marker_$key")

/**
 * Marks this object with the given [key].
 * This is persistent.
 *
 * This function makes sure that there are no
 * conflicts with other plugins, therefore even simple
 * keys are safe.
 */
fun PersistentDataHolder.mark(plugin: Plugin, key: String) {
    persistentDataContainer[markerKey(plugin, key), PersistentDataType.BYTE] = 1.toByte()
}

/**
 * Removes the given [key] from this objects'
 * markings.
 */
fun PersistentDataHolder.unmark(plugin: Plugin, key: String) {
    persistentDataContainer.remove(markerKey(plugin, key))
}

/**
 * @return True, if the given [key] is among
 * this objects' markings.
 */
fun PersistentDataHolder.hasMark(plugin: Plugin, key: String) =
    persistentDataContainer.has(markerKey(plugin, key), PersistentDataType.BYTE)

/** @see PersistentDataHolder.mark */
fun ItemStack.mark(plugin: Plugin, key: String) = meta { mark(plugin, key) }

/** @see PersistentDataHolder.unmark */
fun ItemStack.unmark(plugin: Plugin, key: String) = meta { unmark(plugin, key) }

/** @see PersistentDataHolder.hasMark */
fun ItemStack.hasMark(key: String): Boolean {
    var result: Boolean = false
    meta { result = hasMark(key) }
    return result
}
