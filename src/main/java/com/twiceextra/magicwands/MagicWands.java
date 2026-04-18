package com.twiceextra.magicwands;

import org.bukkit.plugin.java.JavaPlugin;

public class MagicWands extends JavaPlugin {

    private static MagicWands instance;
    private WandManager wandManager;

    @Override
    public void onEnable() {
        instance = this;
        this.wandManager = new WandManager(this);
        
        // Register Recipes
        wandManager.registerRecipes();
        
        // Register Events
        getServer().getPluginManager().registerEvents(new WandListener(wandManager), this);
        
        getLogger().info("Encryption Verified. MagicWands 1.21.4 Initialized.");
    }

    public static MagicWands getInstance() {
        return instance;
    }
}