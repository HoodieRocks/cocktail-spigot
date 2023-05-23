package me.cobble.cocktail.cmds

import dev.jorel.commandapi.kotlindsl.anyResultingExecutor
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.integerArgument
import me.cobble.cocktail.utils.Strings
import org.bukkit.Bukkit

class RandCommand {

  init {
    val suggestionsNames = arrayListOf<String>()
    Bukkit.getServer().scoreboardManager?.mainScoreboard?.objectives?.forEach { suggestionsNames.add(it.name) }

    commandAPICommand("rand") {
      integerArgument("min", Int.MIN_VALUE, Int.MAX_VALUE)
      integerArgument("max", Int.MIN_VALUE, Int.MAX_VALUE)
      anyResultingExecutor { sender, args ->
        if (sender.isOp) {
          if (args.count() == 2) {
            val minValue = args[0] as Int
            val maxValue = args[1] as Int
            val randomValue = (minValue..maxValue).random()

            sender.sendMessage(Strings.color("&eYour number: $randomValue"))
            return@anyResultingExecutor randomValue
          } else {
            sender.sendMessage("Too few arguments, /rand <min> <max>")
            return@anyResultingExecutor 0
          }
        } else {
          sender.sendMessage(Strings.color("&cNo permission!"))
          return@anyResultingExecutor 0
        }
      }
    }
  }
}
