package me.cobble.cocktail.cmds

import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.entitySelectorArgumentOneEntity
import dev.jorel.commandapi.kotlindsl.integerArgument
import dev.jorel.commandapi.kotlindsl.multiLiteralArgument
import me.cobble.cocktail.Cocktail
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import java.util.function.Consumer

class TargetCommand(plugin: Cocktail) {

  init {
    commandAPICommand("target") {
      entitySelectorArgumentOneEntity("entity")
      multiLiteralArgument("set", "clear")
      entitySelectorArgumentOneEntity("target_entity")
      integerArgument("ticks", 0, Int.MAX_VALUE)
      anyExecutor { sender, args ->

        if (!sender.isOp) {
          return@anyExecutor
        }

        val mob = args[0] as Mob
        val targetEntity = args[2] as LivingEntity

        Bukkit.getScheduler().runTaskTimerAsynchronously(
          plugin,
          Consumer {
            when (args[1]) {
              "set" -> {
                mob.target = targetEntity
              }

              "clear" -> {
                mob.target = null
              }

              else -> error("Impossible state!")
            }
          },
          0,
          1,
        )
      }
    }
  }
}
