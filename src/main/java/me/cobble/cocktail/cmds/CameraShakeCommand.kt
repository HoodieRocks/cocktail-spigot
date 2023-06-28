package me.cobble.cocktail.cmds

import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.entitySelectorArgumentOnePlayer
import dev.jorel.commandapi.kotlindsl.floatArgument
import dev.jorel.commandapi.kotlindsl.multiLiteralArgument
import me.cobble.cocktail.Cocktail
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.random.Random

class CameraShakeCommand(private val plugin: Cocktail) {
  init {
    commandAPICommand("camerashake") {
      entitySelectorArgumentOnePlayer("player")
      floatArgument("intensity", 0f, 1f)
      floatArgument("seconds")
      multiLiteralArgument("positional", "rotational", optional = true)
      anyExecutor { sender, args ->
        if(sender.isOp) {
          val player = args[0] as Player
          val intensity = args[1] as Float
          val seconds = args[2] as Float
          val shakeType = args[3] as String?

          val scheduler = Bukkit.getScheduler()

          when(shakeType) {
            "rotational" -> scheduler.runTaskTimerAsynchronously(plugin, Runnable {
                val yaw = player.location.yaw + ((-intensity * 90) + Random.nextFloat() * ((-intensity * 90) - (intensity * 90)))
                val pitch = player.location.pitch + ((-intensity * 90) + Random.nextFloat() * ((-intensity * 90) - (intensity * 90)))

                player.teleport(Location(player.world, player.location.x, player.location.y, player.location.z, yaw, pitch))
              }, 0, (seconds * 20).toLong())
            else -> scheduler.runTaskTimerAsynchronously(plugin, Runnable {
              val x = player.location.x + (-intensity + Random.nextFloat())
              val y = player.location.y + (-intensity + Random.nextFloat())
              val z = player.location.z + (-intensity + Random.nextFloat())

              player.teleport(Location(player.world, x, y, z))
            }, 0, (seconds * 20).toLong())
          }
        }
      }
    }
  }
}
