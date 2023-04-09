package me.cobble.cocktail

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIConfig
import io.papermc.lib.PaperLib
import me.cobble.cocktail.listeners.SendResourcePackListener
import me.cobble.cocktail.utils.CommandRegistry
import me.cobble.cocktail.utils.Config
import me.cobble.cocktail.utils.DatapackUpdater
import me.cobble.cocktail.web.ResourcePackFileServer
import org.bukkit.plugin.java.JavaPlugin

class Cocktail : JavaPlugin() {

  private lateinit var protocolManager: ProtocolManager
  private lateinit var registry: CommandRegistry
  private lateinit var resourcePackFileServer: ResourcePackFileServer

  override fun onLoad() {
    protocolManager = ProtocolLibrary.getProtocolManager()
    registry = CommandRegistry(this, protocolManager)
    CommandAPI.onLoad(CommandAPIConfig().silentLogs(true))
    registry.registerVanilla()
  }

  override fun onEnable() {
    // Plugin startup logic
    this.saveDefaultConfig()
    Config.setup(this)

    CommandAPI.onEnable(this)

    if (PaperLib.isPaper()) {
      logger.warning(
        "Paper and it's forks are known for causing issues with datapacks, " +
          "using Cocktail with Paper works but is not recommended",
      )
    }

    if (Config.getBool("pack-downloader")) DatapackUpdater.run(this)

    resourcePackFileServer = ResourcePackFileServer(this)

    registry.registerSpigot()
    resourcePackFileServer.start()
    SendResourcePackListener(this, resourcePackFileServer)
  }

  override fun onDisable() {
    // Plugin shutdown logic
    CommandAPI.onDisable()
    registry.unregisterVanilla()
    resourcePackFileServer.stop()
  }
}
