package me.cobble.cocktail.cmds

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelector
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.arguments.FunctionArgument
import dev.jorel.commandapi.arguments.TimeArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.wrappers.FunctionWrapper
import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Color
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
                            if (it is FunctionWrapper) Bukkit.getScheduler().runTaskLater(
                                plugin,
                                Runnable { it.runAs(entity) },
                                time.toLong()
                            )
                        }
                    } else {
                        sender.sendMessage(Color.color("&cToo little arguments"))
                    }
                }
            })
            .register()
    }
}