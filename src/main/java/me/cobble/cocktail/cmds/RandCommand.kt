package me.cobble.cocktail.cmds

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.ScoreHolderArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandExecutor
import me.cobble.cocktail.utils.Color
import org.bukkit.Bukkit


class RandCommand {

    init {
        CommandAPICommand("rand")
            .withArguments(IntegerArgument("min", Int.MIN_VALUE, Int.MAX_VALUE))
            .withArguments(IntegerArgument("max", Int.MIN_VALUE, Int.MAX_VALUE))
            .withArguments(StringArgument("name"))
            .withArguments(ScoreHolderArgument<StringArgument>("board"))
            .executes(CommandExecutor { sender, args ->
                if (sender.isOp) {
                    if (args.size == 4) {
                        val min = args[0] as Int
                        val max = args[1] as Int
                        val player = args[2] as String
                        val board = Bukkit.getScoreboardManager()!!.mainScoreboard.getObjective(args[3] as String)!!

                        board.getScore(player).score = (min..max).random()
                    } else {
                        sender.sendMessage("Too few arguments, /random <min> <max> <player> <board>")
                    }
                } else {
                    sender.sendMessage(Color.color("&cNo permission!"))
                }
            })
    }
}
