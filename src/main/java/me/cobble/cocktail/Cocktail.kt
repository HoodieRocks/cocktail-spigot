package me.cobble.cocktail

import me.cobble.cocktail.cmds.*
import me.cobble.cocktail.listeners.OverrideReloadCommandListener
import me.cobble.cocktail.utils.Config
import me.cobble.cocktail.utils.HTTPUtils
import me.cobble.cocktail.utils.Reports
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


class Cocktail : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic

        this.saveDefaultConfig()
        Config.setup()

        if (Config.get().getBoolean("pack-downloader")) HTTPUtils.getDatapacks()

        // TODO: Reimplement Fetch Command when API is available
        // Command API commands
        DamageCommand()
        RandCommand()
        TestCommand()
        TimerCommand(this)
        VelocityCommand()
        CollisionCommand()

        // Spigot API commands
        FlyCommand(this)
        ReportCommand(this)
        CocktailCheck(this)

        // Override /minecraft:reload
        OverrideReloadCommandListener(this)

        Reports.load(this)

        // auto-save
        Bukkit.getScheduler().runTaskTimer(this, Runnable { Reports.save(this) }, 200, 200)

    }

    override fun onDisable() {
        // Plugin shutdown logic
        Reports.save(this)

    }
}
