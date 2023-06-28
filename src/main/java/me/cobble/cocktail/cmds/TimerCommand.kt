package me.cobble.cocktail.cmds

import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.entitySelectorArgumentOneEntity
import dev.jorel.commandapi.kotlindsl.functionArgument
import dev.jorel.commandapi.kotlindsl.timeArgument
import dev.jorel.commandapi.wrappers.FunctionWrapper
import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Strings
import org.bukkit.Bukkit
import org.bukkit.entity.Entity

class TimerCommand(plugin: Cocktail) {

  init {
    commandAPICommand("timer") {
      timeArgument("time")
      entitySelectorArgumentOneEntity("entity")
      functionArgument("function")
      anyExecutor { sender, args ->
        if (sender.isOp) {
          if (args.count() == 3) {
            val time = args[0] as Int
            val entity = args[1] as Entity
            val function = args[2] as Array<*>

            function.forEach {
              // if you receive reports of timer not working correctly, change this back to sync
              if (it is FunctionWrapper) {
                Bukkit.getScheduler().runTaskLaterAsynchronously(
                  plugin,
                  Runnable { it.runAs(entity) },
                  time.toLong(),
                )
              }
            }
          } else {
            sender.sendMessage(Strings.color("&cToo little arguments"))
          }
        }
      }
    }
  }
}
