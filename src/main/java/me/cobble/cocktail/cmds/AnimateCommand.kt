package me.cobble.cocktail.cmds

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolManager
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import dev.jorel.commandapi.executors.CommandExecutor
import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.PacketUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.function.Consumer

class AnimateCommand(plugin: Cocktail, packetManager: ProtocolManager) {

  init {
    CommandAPICommand("animate")
      .withArguments(EntitySelectorArgument.OnePlayer("player"))
      .withArguments(
        MultiLiteralArgument(
          "swing_main",
          "damage",
          "leave_bed",
          "swing_offhand",
          "crit",
          "magic_crit",
        ),
      )
      .withArguments(IntegerArgument("ticks", 0, Int.MAX_VALUE))
      .executes(
        CommandExecutor { sender, args ->
          if (!sender.isOp) {
            sender.sendMessage(ChatColor.RED.toString() + "No permission!")
            return@CommandExecutor
          }

          val animNumber = when (args[1] as String) {
            "swing_main" -> 0
            "damage" -> 1
            "leave_bed" -> 2
            "swing_offhand" -> 3
            "crit" -> 4
            "magic_crit" -> 5
            else -> error("Impossible animation state reached")
          }

          var ticks = 0

          Bukkit.getScheduler().runTaskTimerAsynchronously(
            plugin,
            Consumer {
              val packet = packetManager.createPacket(PacketType.Play.Server.ANIMATION)
              packet.integers.write(0, (args[0] as Player).entityId)
              packet.integers.write(1, animNumber)
              PacketUtils.sendToAll(packetManager, packet)
              if (ticks == args[2] as Int) {
                it.cancel()
              } else {
                ticks++
              }
            },
            0,
            1,
          )
        },
      )
      .register()
  }
}
