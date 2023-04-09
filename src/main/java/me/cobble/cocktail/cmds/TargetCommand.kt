package me.cobble.cocktail.cmds

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import dev.jorel.commandapi.executors.CommandExecutor
import me.cobble.cocktail.Cocktail
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import java.util.function.Consumer

class TargetCommand(plugin: Cocktail) {

  init {
    CommandAPICommand("target")
      .withArguments(EntitySelectorArgument.OneEntity("entity"))
      .withArguments(MultiLiteralArgument("set", "clear"))
      .withArguments(EntitySelectorArgument.OneEntity("target_entity"))
      .withArguments(IntegerArgument("ticks", 0, Int.MAX_VALUE))
      .executes(
        CommandExecutor { sender, args ->

          if (!sender.isOp) {
            return@CommandExecutor
          }

          val mob = args[0] as Mob
          val targetEntity = args[2] as LivingEntity

          Bukkit.getScheduler().runTaskTimerAsynchronously(
            plugin,
            Consumer {
              when (args[1] as String) {
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
        },
      )
      .register()
  }
}
