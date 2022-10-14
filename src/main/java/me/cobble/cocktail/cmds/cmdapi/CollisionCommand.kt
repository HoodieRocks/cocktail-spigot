package me.cobble.cocktail.cmds.cmdapi

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelector
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

class CollisionCommand {

    init {
        CommandAPICommand("collision")
            .withSubcommand(
                CommandAPICommand("enable")
                    .withArguments(EntitySelectorArgument<Collection<Entity>>("entities", EntitySelector.MANY_ENTITIES))
                    .executes(CommandExecutor { sender, args ->
                        if (sender.isOp) {
                            val entities = args[0] as Collection<*>
                            entities.forEach {
                                if (it is LivingEntity) it.isCollidable = true
                            }
                            sender.sendMessage("Hello")
                        }
                    })
            )
            .withSubcommand(
                CommandAPICommand("disable")
                    .withArguments(EntitySelectorArgument<Collection<Entity>>("entities", EntitySelector.MANY_ENTITIES))
                    .executes(CommandExecutor { sender, args ->
                        if (sender.isOp) {
                            val entities = args[0] as Collection<*>
                            entities.forEach {
                                if (it is LivingEntity) it.isCollidable = false
                            }
                            sender.sendMessage("goodbye")
                        }
                    })
            )
            .register()
    }
}