package net.axay.kotlinlanguage;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Only for paper
 */
public class KotlinLanguagePlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        getSLF4JLogger().info("Kotlin language is ready!");
    }


    @Override
    public void onDisable() {
        getSLF4JLogger().warn("Kotlin language plugin disabled! Errors may occur.");
    }
}
