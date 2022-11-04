package me.cobble.cocktail.cmds.function

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import me.cobble.cocktail.utils.Strings

class TestCommand {
    init {
        CommandAPICommand("test")
            .executes(CommandExecutor { sender, _ ->
                sender.sendMessage("Testing utils (randomBytes): ${Strings.randomBytes(8)}")
                sender.sendMessage("Cocktail test complete!")
            })
            .register()
    }
}