package me.cobble.cocktail

import me.cobble.cocktail.gui.MenuListeners
import me.cobble.cocktail.listeners.ResourcesEvents
import me.cobble.cocktail.utils.Config
import me.cobble.cocktail.utils.HTTPUtils
import org.bukkit.plugin.java.JavaPlugin

class Cocktail : JavaPlugin() {
    private lateinit var registry: Registry

    override fun onEnable() {
        // Plugin startup logic
        registry = Registry()

        this.saveDefaultConfig()
        Config.setup()

        HTTPUtils.getDatapacks()

        ResourcesEvents(this)
        MenuListeners(this)

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}