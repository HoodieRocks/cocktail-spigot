package me.cobble.cocktail

import me.cobble.cocktail.cmds.function.*
import me.cobble.cocktail.cmds.nonfunction.CocktailCheck
import me.cobble.cocktail.cmds.nonfunction.FlyCommand
import me.cobble.cocktail.cmds.nonfunction.ReportCommand
import me.cobble.cocktail.listeners.OverrideReloadCommandListener
import me.cobble.cocktail.utils.Config
import me.cobble.cocktail.utils.DatapackUpdater
import me.cobble.cocktail.utils.Reports
import org.bukkit.plugin.java.JavaPlugin


class Cocktail : JavaPlugin() {

    val instance = this

    override fun onEnable() {
        // Plugin startup logic

        this.saveDefaultConfig()
        Config.setup()

        if (Config.getBool("pack-downloader")) DatapackUpdater.run(this)

        // Command API commands
        DamageCommand()
        RandCommand()
        TestCommand()
        TimerCommand(this)
        VelocityCommand()

        // Spigot API commands
        FlyCommand(this)
        ReportCommand(this)
        CocktailCheck(this)

        // Override /minecraft:reload
        OverrideReloadCommandListener(this)

        Reports.load(this)
        Reports.startAutoSave(this)

    }

    override fun onDisable() {
        // Plugin shutdown logic
        Reports.save(this)

    }
}
