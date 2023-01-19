package me.cobble.cocktail.utils

import dev.jorel.commandapi.CommandAPI
import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.cmds.function.*
import me.cobble.cocktail.listeners.OverrideReloadListener

class CommandRegistry(private val plugin: Cocktail) {

    fun registerVanilla() {
        DamageCommand()
        RandCommand()
        TestCommand(plugin)
        BenchCommand(plugin)
        TimerCommand(plugin)
    }

    fun registerSpigot() {

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