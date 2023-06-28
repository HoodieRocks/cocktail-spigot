package me.cobble.cocktail.cmds

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolManager
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.entitySelectorArgumentOnePlayer
import dev.jorel.commandapi.kotlindsl.integerArgument
import org.bukkit.entity.Player

class SetSlotCommand(packetManager: ProtocolManager) {

  init {
    commandAPICommand("setslot") {
      entitySelectorArgumentOnePlayer("player")
      integerArgument("slot", 0, 8)
      anyExecutor { sender, args ->
        if (!sender.isOp) {
          return@anyExecutor
        }
        val packetContainer = packetManager.createPacket(PacketType.Play.Server.HELD_ITEM_SLOT)
        packetContainer.integers.write(0, args[1] as Int)
        packetManager.sendServerPacket(args[0] as Player, packetContainer)
      }
    }
  }
}
