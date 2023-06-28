package me.cobble.cocktail.utils

import com.comphenix.protocol.ProtocolManager
import dev.jorel.commandapi.CommandAPI
import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.cmds.AnimateCommand
import me.cobble.cocktail.cmds.BenchCommand
import me.cobble.cocktail.cmds.RandCommand
import me.cobble.cocktail.cmds.SetSlotCommand
import me.cobble.cocktail.cmds.TargetCommand
import me.cobble.cocktail.cmds.TestCommand
import me.cobble.cocktail.cmds.TimerCommand
import me.cobble.cocktail.cmds.VelocityCommand
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
