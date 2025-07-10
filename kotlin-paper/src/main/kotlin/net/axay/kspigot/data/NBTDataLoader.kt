@file:Suppress("unused")

package net.axay.kspigot.data

import net.axay.kspigot.annotations.NMS_General
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.ProblemReporter.ScopedCollector
import net.minecraft.world.level.storage.TagValueInput
import org.bukkit.craftbukkit.entity.CraftEntity
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger("NBTDataLoader")

@NMS_General
var Entity.nbtData: CompoundTag
    get() {
        val handle = (this as CraftEntity).handle
        val nbtTagCompound = CompoundTag()

        ScopedCollector(handle.problemPath(), LOGGER).use { scopedCollector ->
            handle.load(TagValueInput.create(scopedCollector, handle.registryAccess(), nbtTagCompound))
        }

        return nbtTagCompound
    }
    set(value) {
        val handle = (this as CraftEntity).handle

        ScopedCollector(handle.problemPath(), LOGGER).use { scopedCollector ->
            handle.load(TagValueInput.create(scopedCollector, handle.registryAccess(), value))
        }
    }

@NMS_General
val ItemStack.nbtData: CompoundTag
    get() = (this as? CraftItemStack)?.handle?.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: CompoundTag()