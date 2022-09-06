package me.cobble.cocktail.cmds

import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand

class TestCommand(name: String?, description: String?, usageMessage: String?, aliases: List<String?>?) : BukkitCommand(
    name!!, description!!, usageMessage!!, aliases!!
) {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
        sender.sendMessage("Woohoo!")
        return false
    }
}