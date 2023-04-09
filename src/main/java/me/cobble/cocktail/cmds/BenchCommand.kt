package me.cobble.cocktail.cmds

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.FunctionArgument
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.TimeArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.wrappers.FunctionWrapper
import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Strings
import org.bukkit.Bukkit
import kotlin.system.measureNanoTime

class BenchCommand(private val plugin: Cocktail) {
  init {
    CommandAPICommand("benchmark")
      .withAliases("bench", "functionperf", "functionbench")
      .withSubcommand(
        CommandAPICommand("seconds")
          .withArguments(TimeArgument("time"))
          .withArguments(FunctionArgument("function"))
          .executesPlayer(
            PlayerCommandExecutor { sender, args ->
              if (sender.isOp) {
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
                    sender.sendMessage(Strings.color("&eMean exec. time of ${(function[0] as FunctionWrapper).key}:"))
                    sender.sendMessage(Strings.color("${bigArray.average() / 1e6}ms"))
                  },
                  seconds.toLong(),
                )
              }
            },
          ),
      )
      .withSubcommand(
        CommandAPICommand("times")
          .withArguments(IntegerArgument("number of times"))
          .withArguments(FunctionArgument("function"))
          .executesPlayer(
            PlayerCommandExecutor { sender, args ->
              if (sender.isOp) {
                val times = args[0] as Int
                val function = args[1] as Array<*>

                val startTime = System.nanoTime()
                repeat(times) {
                  function.forEach {
                    if (it is FunctionWrapper) it.run()
                  }
                }

                sender.sendMessage(Strings.color("&eTime to execute ${(function[0] as FunctionWrapper).key} &f$times&e times:"))
                sender.sendMessage(Strings.color("&f${(System.nanoTime() - startTime) / 1e6}ms"))
              }
            },
          ),
      )
      .register()
  }
}
