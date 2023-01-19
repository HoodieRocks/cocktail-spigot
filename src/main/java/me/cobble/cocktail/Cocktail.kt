package me.cobble.cocktail

import io.papermc.lib.PaperLib
import me.cobble.cocktail.utils.CommandRegistry
import me.cobble.cocktail.utils.Config
import me.cobble.cocktail.utils.DatapackUpdater
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
        if (PaperLib.isPaper()) {
            logger.warning(
                "Paper and it's forks are known for causing issues with datapacks, " +
                        "using Cocktail with Paper works but is not recommended"
            )
        }

        if (Config.getBool("pack-downloader")) DatapackUpdater.run(this)

        registry.registerSpigot()

    }

    override fun onDisable() {
        // Plugin shutdown logic
        registry.unregisterVanilla()
    }
}
