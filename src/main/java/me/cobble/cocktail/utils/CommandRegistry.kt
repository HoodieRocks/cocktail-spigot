package me.cobble.cocktail.utils

import de.tr7zw.nbtapi.NBTContainer
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIConfig
import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.cmds.function.*
import me.cobble.cocktail.cmds.nonfunction.FlySpeedCommand
import me.cobble.cocktail.cmds.nonfunction.ReportCommand
import me.cobble.cocktail.listeners.OverrideReloadListener

class CommandRegistry(private val plugin: Cocktail) {

    fun registerVanilla() {
        CommandAPI.onLoad(CommandAPIConfig().initializeNBTAPI(NBTContainer::class.java, ::NBTContainer))
        DamageCommand()
        RandCommand()
        TestCommand(plugin)
        BenchCommand(plugin)
        TimerCommand(plugin)
        PDataCommand()
    }

    fun registerSpigot() {

        FlySpeedCommand(plugin)
        ReportCommand(plugin)

        // uses Bukkit object, so it's required to registered in JavaPlugin#onEnable() instead
        VelocityCommand()

        OverrideReloadListener(plugin)
    }

    fun unregisterVanilla() {
        CommandAPI.unregister("damage")
        CommandAPI.unregister("rand")
        CommandAPI.unregister("ok")
        CommandAPI.unregister("bench")
        CommandAPI.unregister("timer")
        CommandAPI.unregister("pdata")
        CommandAPI.unregister("velocity")
    }
}