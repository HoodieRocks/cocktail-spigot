package me.cobble.cocktail.cmds

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand

class RandCommand : BukkitCommand("rand") {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if(args.size == 4) {
            val min = args[0].toInt()
            val max = args[1].toInt()
            val player = Bukkit.getPlayer(args[2])!!
            val board = Bukkit.getScoreboardManager()!!.mainScoreboard.getObjective(args[3])!!

            board.getScore(player.name).score = (min..max).random()
            return true
        } else {
            sender.sendMessage("Too few arguments, /random <min> <max> <player> <board>")
        }
        return false
    }

}