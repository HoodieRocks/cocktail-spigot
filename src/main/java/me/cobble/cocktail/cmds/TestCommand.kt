package me.cobble.cocktail.cmds

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor

class TestCommand {
    init {
        CommandAPICommand("test")
            .executes(CommandExecutor { sender, _ -> sender.sendMessage("Cocktail test complete!") })
    }
}