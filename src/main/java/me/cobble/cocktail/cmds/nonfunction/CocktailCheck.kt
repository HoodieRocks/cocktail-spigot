package me.cobble.cocktail.cmds.nonfunction

import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Strings
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class CocktailCheck(private val plugin: Cocktail) : CommandExecutor {
    init {
        plugin.getCommand("ctversion")!!.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender.isOp) {
            sender.sendMessage(Strings.color("&dRunning Cocktail v${plugin.description.version}"))
            return true
        }
        return false
    }
}
