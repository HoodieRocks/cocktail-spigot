package me.cobble.cocktail

import me.cobble.cocktail.cmds.FlyCommand
import me.cobble.cocktail.cmds.ReportCommand
import me.cobble.cocktail.gui.MenuListeners
import me.cobble.cocktail.listeners.OverrideReloadCommandListener
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

        registry.registerCommands()

        HTTPUtils.getDatapacks()

        FlyCommand(this)
        ReportCommand(this)

        ResourcesEvents(this)
        MenuListeners(this)
        OverrideReloadCommandListener(this)

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}