package me.cobble.cocktail.cmds.spigot

import me.cobble.cocktail.Cocktail
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class CocktailCheck(private val plugin: Cocktail) : CommandExecutor {
    init {
        plugin.getCommand("ctversion")!!.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender.isOp) {
            sender.sendMessage(plugin.description.version)
            return true
        }
        return false
    }
}