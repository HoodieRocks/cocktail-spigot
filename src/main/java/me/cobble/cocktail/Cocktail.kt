package me.cobble.cocktail

import me.cobble.cocktail.utils.CommandRegistry
import me.cobble.cocktail.utils.Config
import me.cobble.cocktail.utils.DatapackUpdater
import me.cobble.cocktail.utils.Reports
import org.bukkit.plugin.java.JavaPlugin


class Cocktail : JavaPlugin() {

    private val registry = CommandRegistry(this)

    override fun onLoad() {
        registry.registerVanilla()
    }

    override fun onEnable() {
        // Plugin startup logic

        this.saveDefaultConfig()
        Config.setup(this)

        if (Config.getBool("pack-downloader")) DatapackUpdater.run(this)

        registry.registerSpigot()

        Reports.load(this)
        Reports.startAutoSave(this)

    }

    override fun onDisable() {
        // Plugin shutdown logic
        registry.unregisterVanilla()

        Reports.save(this)
    }
}
