package me.cobble.cocktail

import me.cobble.cocktail.cmds.cmdapi.DamageCommand
import me.cobble.cocktail.cmds.cmdapi.RandCommand
import me.cobble.cocktail.cmds.cmdapi.TimerCommand
import me.cobble.cocktail.cmds.cmdapi.VelocityCommand
import me.cobble.cocktail.cmds.spigot.CocktailCheck
import me.cobble.cocktail.cmds.spigot.FlyCommand
import me.cobble.cocktail.cmds.spigot.ReportCommand
import me.cobble.cocktail.cmds.spigot.TestCommand
import me.cobble.cocktail.listeners.OverrideReloadCommandListener
import me.cobble.cocktail.utils.Config
import me.cobble.cocktail.utils.HTTPUtils
import me.cobble.cocktail.utils.Reports
import org.bukkit.plugin.java.JavaPlugin


class Cocktail : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic

        this.saveDefaultConfig()
        Config.setup()

        if (Config.getBool("pack-downloader")) HTTPUtils.getDatapacks()

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
