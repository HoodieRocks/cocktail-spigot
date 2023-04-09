package me.cobble.cocktail.listeners

import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Config
import me.cobble.cocktail.utils.DatapackUpdater
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class OverrideReloadListener(private val plugin: Cocktail) : Listener {

  init {
    Bukkit.getServer().pluginManager.registerEvents(this, plugin)
  }

  @EventHandler
  fun onReload(e: PlayerCommandPreprocessEvent) {
    if (e.message == "/minecraft:reload" && e.player.isOp && !Config.getBool("unbind-reload")) {
      e.isCancelled = true
      e.player.sendMessage("Reloading Cocktail & Datapacks")
      Config.reload()
      DatapackUpdater.run(plugin)
      e.player.sendMessage("Reloading Complete")
      e.player.setResourcePack("${Bukkit.getIp()}:")
    }
  }
}
