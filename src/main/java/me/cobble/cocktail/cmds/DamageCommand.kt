package me.cobble.cocktail.cmds

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand

class DamageCommand : BukkitCommand("damage") {

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if(args.size == 2) {
            val player = Bukkit.getPlayer(args[0])!!
            val damage = args[1].toDouble()

            player.damage(damage)

            return true
        } else {
            sender.sendMessage("Too few arguments, /random <player> <amount>")
        }
        return false
    }

}