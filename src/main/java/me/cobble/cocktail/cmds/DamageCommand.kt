package me.cobble.cocktail.cmds

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.DoubleArgument
import dev.jorel.commandapi.arguments.EntitySelector
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.executors.CommandExecutor
import me.cobble.cocktail.utils.Color
import org.bukkit.entity.Player

class DamageCommand {
    init {
        CommandAPICommand("damage")
            .withArguments(EntitySelectorArgument<Player>("player", EntitySelector.ONE_PLAYER))
            .withArguments(DoubleArgument("damage"))
            .executes(CommandExecutor { sender, args ->
                if (sender.isOp) {
                    if (args.size == 2) {
                        val player = args[0] as Player
                        val damage = args[1] as Double

                        player.damage(damage)
                        return@CommandExecutor
                    } else {
                        sender.sendMessage("Too few arguments, /random <player> <amount>")
                        return@CommandExecutor
                    }
                } else {
                    sender.sendMessage(Color.color("&cNo permission!"))
                    return@CommandExecutor
                }
            })
            .register()
    }
}
