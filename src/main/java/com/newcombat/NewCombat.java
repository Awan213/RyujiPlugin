package com.newcombat;

import org.bukkit.plugin.java.JavaPlugin;

public class NewCombat extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("NewCombat enabled.");
        new ReturnByDeath(this); // Register passive
        // Tambah skill setup di sini...
    }

    @Override
    public void onDisable() {
        getLogger().info("NewCombat disabled.");
    }
}
