package me.abdulhamit.terraingenerator;

import org.bukkit.plugin.java.JavaPlugin;

public final class TerrainGenerator extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("generate").setExecutor(new Generate());
        getCommand("resetseed").setExecutor(new ResetSeed());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
