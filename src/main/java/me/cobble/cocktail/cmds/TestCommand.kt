package me.cobble.cocktail.cmds

import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Strings

class TestCommand(private val plugin: Cocktail) {
  init {
    commandAPICommand("ok") {
      withAliases("cocktail", "ct")
      subcommand("test") {
        anyExecutor { sender, _ ->
          if (sender.isOp) {
            sender.sendMessage(Strings.color("&dTesting utils (randomBytes): ${Strings.randomBytes(8)}"))
            sender.sendMessage(Strings.color("&aCocktail test complete!"))
          }
        }
        anyExecutor { sender, _ ->
          if (sender.isOp) {
            sender.sendMessage(Strings.color("&dRunning Cocktail v${plugin.description.version}"))
          }
        }
      }
    }
  }
}
