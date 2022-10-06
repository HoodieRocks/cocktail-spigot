package me.cobble.cocktail.cmds

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.DoubleArgument
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

class VelocityCommand {

    init {
        CommandAPICommand("velocity")
            .withSubcommand(
                CommandAPICommand("relative")
                    .withArguments(DoubleArgument("x"))
                    .withArguments(DoubleArgument("y"))
                    .withArguments(DoubleArgument("z"))
                    .executes(CommandExecutor { sender, args ->
                        if(sender is Entity) {
                            val x = args[0] as Double
                            val y = args[1] as Double
                            val z = args[2] as Double
                            val direction = sender.location.direction
                            sender.velocity = Vector(direction.x * x,direction.y * y,direction.z * z)
                        }
                    })
            )
            .withSubcommand(
                CommandAPICommand("aligned")
                    .withArguments(DoubleArgument("x"))
                    .withArguments(DoubleArgument("y"))
                    .withArguments(DoubleArgument("z"))
                    .executes(CommandExecutor { sender, args ->
                        if(sender is Entity) {
                            val x = args[0] as Double
                            val y = args[1] as Double
                            val z = args[2] as Double
                            sender.velocity = Vector(x,y,z)
                        }
                    })
            )

            .register()
    }
}