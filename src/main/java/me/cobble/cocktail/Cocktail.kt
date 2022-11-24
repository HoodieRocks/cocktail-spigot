package me.cobble.cocktail

import me.cobble.cocktail.cmds.function.*
import me.cobble.cocktail.cmds.nonfunction.FlySpeedCommand
import me.cobble.cocktail.cmds.nonfunction.ReportCommand
import me.cobble.cocktail.listeners.OverrideReloadListener
import me.cobble.cocktail.utils.Config
import me.cobble.cocktail.utils.DatapackUpdater
import me.cobble.cocktail.utils.Reports
import org.bukkit.plugin.java.JavaPlugin


class Cocktail : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic

        this.saveDefaultConfig()
        Config.setup(this)

        if (Config.getBool("pack-downloader")) DatapackUpdater.run(this)

        // Command API commands
        DamageCommand()
        RandCommand()
        TestCommand(this)
        BenchCommand(this)
        TimerCommand(this)
        VelocityCommand()

        // Spigot API commands
        FlySpeedCommand(this)
        ReportCommand(this)

        // Override /minecraft:reload
        OverrideReloadListener(this)

        Reports.load(this)
        Reports.startAutoSave(this)

    }

    override fun onDisable() {
        // Plugin shutdown logic
        Reports.save(this)
    }
}
