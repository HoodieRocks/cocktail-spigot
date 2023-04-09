package me.cobble.cocktail.cmds

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolManager
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.entity.Player

class SetSlotCommand(packetManager: ProtocolManager) {

  init {
    CommandAPICommand("setslot")
      .withArguments(EntitySelectorArgument.OnePlayer("player"))
      .withArguments(IntegerArgument("slot", 0, 8))
      .executes(
        CommandExecutor { sender, args ->
          if (!sender.isOp) {
            return@CommandExecutor
          }
          val packetContainer = packetManager.createPacket(PacketType.Play.Server.HELD_ITEM_SLOT)
          packetContainer.integers.write(0, args[1] as Int)
          packetManager.sendServerPacket(args[0] as Player, packetContainer)
        },
      )
      .register()
  }
}
