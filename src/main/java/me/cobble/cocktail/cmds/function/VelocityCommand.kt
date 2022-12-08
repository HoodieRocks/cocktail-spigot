package me.cobble.cocktail.cmds.function

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.ProxyCommandExecutor
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

class VelocityCommand {

    init {
        val precisionExtender = 1000
        val scoreboard = Bukkit.getServer().scoreboardManager?.mainScoreboard!!
        val suggestions = ArgumentSuggestions.strings { scoreboard.objectives.map { it.name }.toTypedArray() }


        // entity.location.direction.multiply(z).add(Vector(0.0, y, 0.0)).add(Vector(0.0, 0.0, x))
        // just saving it for later :)
        CommandAPICommand("velocity")
            .withSubcommand(
                CommandAPICommand("relative")
                    .withArguments(EntitySelectorArgument<Collection<Entity>>("entities", EntitySelector.MANY_ENTITIES))
                    .withArguments(DoubleArgument("x"))
                    .withArguments(DoubleArgument("y"))
                    .withArguments(DoubleArgument("z"))
                    .executes(CommandExecutor { sender, args ->
                        if (sender.isOp) {
                            val entities = args[0] as ArrayList<*>
                            val x = args[1] as Double
                            val y = args[2] as Double
                            val z = args[3] as Double
                            entities.forEach { entity ->
                                if (entity is Entity) {
                                    // repetitive but required?
                                    val rotX = entity.location.direction.rotateAroundY(Math.toRadians(90.0)).multiply(x)
                                    val rotY = entity.location.direction.rotateAroundX(Math.toRadians(90.0)).multiply(y)
                                    val rotZ = entity.location.direction.multiply(z)
                                    entity.velocity = rotZ.add(rotX).add(rotY) // Y is STILL broken
                                }
                            }
                        }

                    })
                    .executesProxy(ProxyCommandExecutor { sender, args ->
                        if (sender.caller.isOp) {
                            val entities = args[0] as ArrayList<*>
                            val x = args[1] as Double
                            val y = args[2] as Double
                            val z = args[3] as Double
                            entities.forEach { entity ->
                                if (entity is Entity) {
                                    val direction = entity.location.direction
                                    val rotatedX = direction.rotateAroundY(Math.toRadians(90.0)).multiply(x)
                                    val rotatedY = direction.rotateAroundX(Math.toRadians(90.0)).multiply(y)
                                    val rotatedZ = direction.multiply(z)
                                    entity.velocity = rotatedZ.add(rotatedX).add(rotatedY)
                                }
                            }
                        }

                    })
            )
            .withSubcommand(
                CommandAPICommand("aligned")
                    .withArguments(EntitySelectorArgument<Collection<Entity>>("entities", EntitySelector.MANY_ENTITIES))
                    .withArguments(DoubleArgument("x"))
                    .withArguments(DoubleArgument("y"))
                    .withArguments(DoubleArgument("z"))
                    .executes(CommandExecutor { sender, args ->
                        if (sender.isOp) {
                            val entities = args[0] as ArrayList<*>
                            val x = args[1] as Double
                            val y = args[2] as Double
                            val z = args[3] as Double
                            entities.forEach { entity ->
                                if (entity is Entity) {
                                    entity.velocity = Vector(x, y, z)
                                }
                            }
                        }

                    })
                    .executesProxy(ProxyCommandExecutor { sender, args ->
                        if (sender.caller.isOp) {
                            val entities = args[0] as ArrayList<*>
                            val x = args[1] as Double
                            val y = args[2] as Double
                            val z = args[3] as Double
                            entities.forEach { entity ->
                                if (entity is Entity) {
                                    entity.velocity = Vector(x, y, z)
                                }
                            }
                        }

                    })
            )
            .withSubcommand(
                CommandAPICommand("scoreboard")
                    .withArguments(EntitySelectorArgument<Collection<Entity>>("entities", EntitySelector.MANY_ENTITIES))
                    .withArguments(ObjectiveArgument("board").replaceSafeSuggestions { suggestions })
                    .executes(CommandExecutor { sender, args ->
                        if (sender.isOp) {
                            val entities = args[0] as ArrayList<*>
                            val board = scoreboard.getObjective(args[1] as String)!!
                            val x = board.getScore("x").score.toDouble() / precisionExtender
                            val y = board.getScore("y").score.toDouble() / precisionExtender
                            val z = board.getScore("z").score.toDouble() / precisionExtender
                            entities.forEach { entity ->
                                if (entity is Entity) {
                                    entity.velocity = Vector(x, y, z)
                                }
                            }
                        }
                    })
                    .executesProxy(ProxyCommandExecutor { sender, args ->
                        if (sender.caller.isOp) {
                            val entities = args[0] as ArrayList<*>
                            val board = scoreboard.getObjective(args[1] as String)!!
                            val x = board.getScore("x").score.toDouble() / precisionExtender
                            val y = board.getScore("y").score.toDouble() / precisionExtender
                            val z = board.getScore("z").score.toDouble() / precisionExtender
                            entities.forEach { entity ->
                                if (entity is Entity) {
                                    entity.velocity = Vector(x, y, z)
                                }
                            }
                        }
                    })
            )

            .register()
    }
}