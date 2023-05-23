package me.cobble.cocktail.utils

import com.comphenix.protocol.ProtocolManager
import dev.jorel.commandapi.CommandAPI
import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.cmds.*
import me.cobble.cocktail.listeners.OverrideReloadListener

class CommandRegistry(private val plugin: Cocktail, private val packetManager: ProtocolManager) {

  fun registerVanilla() {
    RandCommand()
    TestCommand(plugin)
    BenchCommand(plugin)
    TimerCommand(plugin)
    SetSlotCommand(packetManager)
    AnimateCommand(plugin, packetManager)
    TargetCommand(plugin)
  }

  fun registerSpigot() {
    // uses Bukkit object, so it's required to registered in JavaPlugin#onEnable() instead
    VelocityCommand()

    OverrideReloadListener(plugin)
  }

  fun unregisterVanilla() {
    CommandAPI.unregister("rand")
    CommandAPI.unregister("ok")
    CommandAPI.unregister("bench")
    CommandAPI.unregister("timer")
    CommandAPI.unregister("velocity")
    CommandAPI.unregister("setslot")
    CommandAPI.unregister("animate")
    CommandAPI.unregister("target")
  }
}
