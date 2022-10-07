package me.cobble.cocktail.cmds

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.executors.ResultingCommandExecutor
import me.cobble.cocktail.utils.Strings
import org.bukkit.Bukkit


class RandCommand {

    init {
        val suggestionsNames = arrayListOf<String>()
        Bukkit.getServer().scoreboardManager?.mainScoreboard?.objectives?.forEach { suggestionsNames.add(it.name) }

        CommandAPICommand("rand")
            .withArguments(IntegerArgument("min", Int.MIN_VALUE, Int.MAX_VALUE))
            .withArguments(IntegerArgument("max", Int.MIN_VALUE, Int.MAX_VALUE))
            .executes(ResultingCommandExecutor { sender, args ->
                if (sender.isOp) {
                    if (args.size == 2) {
                        val minValue = args[0] as Int
                        val maxValue = args[1] as Int

                        return@ResultingCommandExecutor (minValue..maxValue).random()
                    } else {
                        sender.sendMessage("Too few arguments, /rand <min> <max>")
                        return@ResultingCommandExecutor 0
                    }
                } else {
                    sender.sendMessage(Strings.color("&cNo permission!"))
                    return@ResultingCommandExecutor 0
                }
            }).register()
    }
}
