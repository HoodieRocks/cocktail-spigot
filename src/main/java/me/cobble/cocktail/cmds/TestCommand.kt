package me.cobble.cocktail.cmds

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Strings

class TestCommand(private val plugin: Cocktail) {
  init {
    CommandAPICommand("ok")
      .withAliases("cocktail", "ct")
      .withSubcommand(
        CommandAPICommand("test")
          .executes(
            CommandExecutor { sender, _ ->
              if (sender.isOp) {
                sender.sendMessage(Strings.color("&dTesting utils (randomBytes): ${Strings.randomBytes(8)}"))
                sender.sendMessage(Strings.color("&aCocktail test complete!"))
              }
            },
          ),
      ).executes(
        CommandExecutor { sender, _ ->
          if (sender.isOp) {
            sender.sendMessage(Strings.color("&dRunning Cocktail v${plugin.description.version}"))
          }
        },
      )
      .register()
  }
}
