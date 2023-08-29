package world.kitpvp.kotlinlanguage;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Only for paper
 */
public class KotlinLanguagePlugin extends JavaPlugin {
    @Override
    public void onDisable() {
        getSLF4JLogger().warn("Kotlin language plugin disabled! Errors may occur.");
    }
}
