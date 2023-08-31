package net.axay.kspigot.commands

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack

/**
 * Creates a new command.
 * Neeeds to be registered using `plugin.bridiganderSupport.commands += this`
 *
 * @param name the name of the root command
 */
inline fun command(
    name: String,
    builder: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
): LiteralArgumentBuilder<CommandSourceStack> =
    LiteralArgumentBuilder.literal<CommandSourceStack>(name).apply(builder)

/**
 * Adds a new literal to this command.
 *
 * @param name the name of the literal
 */
inline fun ArgumentBuilder<CommandSourceStack, *>.literal(
    name: String,
    builder: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit = {}
) = command(name, builder).also { then(it) }
