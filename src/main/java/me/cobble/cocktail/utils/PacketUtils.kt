package me.cobble.cocktail.utils

import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Bukkit

object PacketUtils {
  fun sendToAll(manager: ProtocolManager, packet: PacketContainer) {
    Bukkit.getOnlinePlayers().forEach {
      manager.sendServerPacket(it, packet)
    }
  }
}
