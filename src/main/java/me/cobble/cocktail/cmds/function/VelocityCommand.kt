package me.cobble.cocktail.cmds.function

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.ProxyCommandExecutor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

class VelocityCommand {

    init {
        val precisionExtender = 1000
        val suggestionsNames = arrayListOf<String>()
        val scoreboard = Bukkit.getServer().scoreboardManager?.mainScoreboard!!
        scoreboard.objectives.forEach { suggestionsNames.add(it.name) }
        val suggestions = ArgumentSuggestions.strings { suggestionsNames.toTypedArray() }


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
                                if(entity is Entity) {
                                    val direction = entity.location.direction
                                    val rotatedX = direction.rotateAroundY(Math.toRadians(90.0)).multiply(x)
                                    val rotatedY = direction.rotateAroundX(Math.toRadians(90.0)).multiply(y)
                                    val rotatedZ = direction.multiply(z)
                                    entity.velocity = rotatedZ.add(rotatedX).add(rotatedY)
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
                                if(entity is Entity) {
                                    val direction = entity.location.direction
                                    entity.velocity = Vector(direction.x * x, direction.y * y, direction.z * z)
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
                                if(entity is Entity) {
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
                                if(entity is Entity) {
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
                                if(entity is Entity) {
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
                                if(entity is Entity) {
                                    entity.velocity = Vector(x, y, z)
                                }
                            }
                        }
                    })
            )

            .register()
    }

    fun getLocal(reference: Location, local: Vector): Vector {
        // Firstly a vector facing YAW = 0, on the XZ plane as start base
        val axisBase = Vector(0, 0, 1)
        // This one pointing YAW + 90° should be the relative "left" of the field of view,
        // isn't it (since ROLL always is 0°)?
        val axisLeft = axisBase.clone().rotateAroundY(Math.toRadians((-reference.yaw + 90.0f).toDouble()))
        // Left axis should be the rotation axis for going up, too, since it's perpendicular...
        val axisUp: Vector = reference.direction.clone().rotateAroundNonUnitAxis(axisLeft, Math.toRadians(-90.0))

        // Based on these directions, we got all we need
        val sway = axisLeft.clone().normalize().multiply(local.x)
        val heave = axisUp.clone().normalize().multiply(local.y)
        val surge: Vector = reference.direction.clone().multiply(local.z)

        // Add up the global reference based result
        return Vector(reference.x, reference.y, reference.z).add(sway).add(heave).add(surge)
    }
}