package me.cobble.cocktail.cmds.cmdapi

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.DoubleArgument
import dev.jorel.commandapi.arguments.ObjectiveArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.ProxyCommandExecutor
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

class VelocityCommand {

    init {
        val suggestionsNames = arrayListOf<String>()
        Bukkit.getServer().scoreboardManager?.mainScoreboard?.objectives?.forEach { suggestionsNames.add(it.name) }
        val suggestions = ArgumentSuggestions.strings(*suggestionsNames.toTypedArray())

        CommandAPICommand("velocity")
            .withSubcommand(
                CommandAPICommand("relative")
                    .withArguments(DoubleArgument("x"))
                    .withArguments(DoubleArgument("y"))
                    .withArguments(DoubleArgument("z"))
                    .executes(CommandExecutor { sender, args ->
                        if (sender.isOp && sender is Entity) {
                            val x = args[0] as Double
                            val y = args[1] as Double
                            val z = args[2] as Double
                            val direction = sender.location.direction
                            sender.velocity = Vector(direction.x * x, direction.y * y, direction.z * z)
                        }

                    })
                    .executesProxy(ProxyCommandExecutor { sender, args ->
                        if (sender.caller.isOp && sender.callee is Entity) {
                            val x = args[0] as Double
                            val y = args[1] as Double
                            val z = args[2] as Double
                            val direction = (sender.callee as Entity).location.direction
                            (sender.callee as Entity).velocity =
                                Vector(direction.x * x, direction.y * y, direction.z * z)
                        }

                    })
            )
            .withSubcommand(
                CommandAPICommand("aligned")
                    .withArguments(DoubleArgument("x"))
                    .withArguments(DoubleArgument("y"))
                    .withArguments(DoubleArgument("z"))
                    .executes(CommandExecutor { sender, args ->
                        if (sender.isOp && sender is Entity) {
                            val x = args[0] as Double
                            val y = args[1] as Double
                            val z = args[2] as Double
                            sender.velocity = Vector(x, y, z)
                        }

                    })
                    .executesProxy(ProxyCommandExecutor { sender, args ->
                        if (sender.caller.isOp && sender.callee is Entity) {
                            val x = args[0] as Double
                            val y = args[1] as Double
                            val z = args[2] as Double
                            (sender.callee as Entity).velocity = Vector(x, y, z)
                        }

                    })
            )
            .withSubcommand(
                CommandAPICommand("scoreboard")
                    .withArguments(ObjectiveArgument("board").replaceSafeSuggestions { suggestions })
                    .executes(CommandExecutor { sender, args ->
                        if (sender.isOp && sender is Entity) {
                            val board =
                                Bukkit.getScoreboardManager()!!.mainScoreboard.getObjective(args[0] as String)!!
                            val x = board.getScore("x").score.toDouble() / 1000
                            val y = board.getScore("y").score.toDouble() / 1000
                            val z = board.getScore("z").score.toDouble() / 1000
                            sender.velocity = Vector(x, y, z)
                        }
                    })
                    .executesProxy(ProxyCommandExecutor { sender, args ->
                        if (sender.caller.isOp && sender.callee is Entity) {
                            val board = Bukkit.getScoreboardManager()!!.mainScoreboard.getObjective(args[0] as String)!!
                            val x = board.getScore("x").score.toDouble() / 1000
                            val y = board.getScore("y").score.toDouble() / 1000
                            val z = board.getScore("z").score.toDouble() / 1000
                            (sender.callee as Entity).velocity = Vector(x, y, z)
                        }
                    })
            )

            .register()
    }
}