package me.cobble.cocktail.web

import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Config
import org.bukkit.Bukkit
import org.http4k.routing.ResourceLoader
import org.http4k.routing.static
import org.http4k.server.Http4kServer
import org.http4k.server.Undertow
import org.http4k.server.asServer
import java.io.File
import java.security.MessageDigest

class ResourcePackFileServer(private val plugin: Cocktail) {

  private val configPath = "resource-pack-server"
  private lateinit var httpServer: Http4kServer
  private val selectedPack = Config.get().getString("$configPath.active-resource-pack")
  private val port = Config.getInt("resource-pack-server.port")
  private val packServerPath = "${Bukkit.getPluginManager().getPlugin("Cocktail")!!.dataFolder}/pack_server/"

  fun start() {
    if (!Config.getBool("$configPath.enabled")) {
      return
    }
    plugin.logger.info("Starting resource pack server...")
    httpServer = static(ResourceLoader.Directory(packServerPath)).asServer(Undertow(port)).start()
  }

  fun stop() {
    if (!Config.getBool("$configPath.enabled")) {
      return
    }
    plugin.logger.info("Shutting down resource pack server...")
    httpServer.stop()
  }

  fun getResourcePackURL(): String {
    return "${Bukkit.getIp()}:$port/$selectedPack-resources.zip"
  }

  fun getHash(): ByteArray {
    val md: MessageDigest = MessageDigest.getInstance("SHA-1")
    return md.digest(File("$packServerPath/$selectedPack-resources.zip").readBytes().copyOf(20))
  }
}
