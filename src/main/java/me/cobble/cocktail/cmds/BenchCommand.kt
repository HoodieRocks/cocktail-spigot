package me.cobble.cocktail.cmds

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.functionArgument
import dev.jorel.commandapi.kotlindsl.integerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.jorel.commandapi.kotlindsl.timeArgument
import dev.jorel.commandapi.wrappers.FunctionWrapper
import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Strings
import org.bukkit.Bukkit
import kotlin.system.measureNanoTime

class BenchCommand(private val plugin: Cocktail) {
  init {
    commandAPICommand("benchmark") {
      withAliases("bench", "functionperf", "functionbench")
      subcommand("seconds") {
        timeArgument("time")
        functionArgument("function")
        playerExecutor { player, args ->
          if (player.isOp) {
            val seconds = args[0] as Int
            val function = args[1] as Array<*>
            val bigArray = linkedSetOf<Long>()

            val taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {

              bigArray.add(
                measureNanoTime {
                  function.forEach {
                    if (it is FunctionWrapper) it.run()
                  }
                },
              )
            }, 0, 1)

            Bukkit.getScheduler().runTaskLater(
              plugin,
              Runnable {
                Bukkit.getScheduler().cancelTask(taskID)
                player.sendMessage(Strings.color("&eMean exec. time of ${(function[0] as FunctionWrapper).key}:"))
                player.sendMessage(Strings.color("${bigArray.average() / 1e6}ms"))
              },
              seconds.toLong(),
            )
          }
        }
      }
      subcommand("times") {
        integerArgument("number of times")
        functionArgument("function")
        playerExecutor { player, args ->
          if (player.isOp) {
            val times = args[0] as Int
            val function = args[1] as Array<*>

            val startTime = System.nanoTime()
            repeat(times) {
              function.forEach {
                if (it is FunctionWrapper) it.run()
              }
            }

            player.sendMessage(Strings.color("&eTime to execute ${(function[0] as FunctionWrapper).key} &f$times&e times:"))
            player.sendMessage(Strings.color("&f${(System.nanoTime() - startTime) / 1e6}ms"))
          }
        }
      }
    }
  }
}
