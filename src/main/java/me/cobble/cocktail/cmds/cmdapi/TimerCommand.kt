package me.cobble.cocktail.cmds.cmdapi

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelector
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.arguments.FunctionArgument
import dev.jorel.commandapi.arguments.TimeArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.wrappers.FunctionWrapper
import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Strings
import org.bukkit.Bukkit
import org.bukkit.entity.Entity

class TimerCommand(plugin: Cocktail) {

    init {
        CommandAPICommand("timer")
            .withArguments(TimeArgument("time"))
            .withArguments(EntitySelectorArgument<Entity>("entities", EntitySelector.ONE_ENTITY))
            .withArguments(FunctionArgument("function"))
            .executes(CommandExecutor { sender, args ->
                if (sender.isOp) {
                    if (args.size == 3) {
                        val time = args[0] as Int
                        val entity = args[1] as Entity
                        val function = args[2] as Array<*>

                        function.forEach {
                            // if you receive reports of timer not working correctly, change this back to sync
                            if (it is FunctionWrapper) Bukkit.getScheduler().runTaskLaterAsynchronously(
                                plugin,
                                Runnable { it.runAs(entity) },
                                time.toLong()
                            )
                        }
                    } else {
                        sender.sendMessage(Strings.color("&cToo little arguments"))
                    }
                }
            })
            .register()
    }
}
