package me.cobble.cocktail.cmds

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.DoubleArgument
import dev.jorel.commandapi.arguments.EntitySelector
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.executors.CommandExecutor
import me.cobble.cocktail.utils.Color
import org.bukkit.entity.Damageable
import org.bukkit.entity.Entity

class DamageCommand {
    init {
        CommandAPICommand("damage")
            .withArguments(EntitySelectorArgument<Collection<Entity>>("entities", EntitySelector.MANY_ENTITIES))
            .withArguments(DoubleArgument("damage"))
            .executes(CommandExecutor { sender, args ->
                if (sender.isOp) {
                    if (args.size == 2) {
                        val entity = args[0] as ArrayList<*>
                        val damage = args[1] as Double

                        entity.forEach { if (it is Damageable) it.damage(damage) }
                        return@CommandExecutor
                    } else {
                        sender.sendMessage("Too few arguments, /damage <selector> <amount>")
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
