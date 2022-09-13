package me.cobble.cocktail.cmds

import me.cobble.cocktail.Cocktail
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlyCommand(plugin: Cocktail) : CommandExecutor {

    init {
        plugin.getCommand("flyspeed")!!.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender.isOp && sender is Player) {
            if (args.size == 1) {
                sender.flySpeed = (args[0].toFloat() / 10f)
                sender.sendMessage("Your fly speed is now ${args[0]}")
                return true
            } else sender.sendMessage("Please supply a number")
        } else sender.sendMessage("No permission")

        return false
    }
}