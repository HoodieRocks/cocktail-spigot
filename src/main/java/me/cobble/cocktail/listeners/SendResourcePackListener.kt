package me.cobble.cocktail.listeners

import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Config
import me.cobble.cocktail.web.ResourcePackFileServer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class SendResourcePackListener(plugin: Cocktail, private val packServer: ResourcePackFileServer) : Listener {

  init {
    Bukkit.getPluginManager().registerEvents(this, plugin)
  }

  @EventHandler
  fun onJoin(e: PlayerJoinEvent) {
    if (Config.getBool("resource-pack-server.enabled")) {
      e.player.setResourcePack(
        packServer.getResourcePackURL(),
        packServer.getHash(),
        true,
      )
    }
  }
}
