package me.cobble.cocktail.cmds.function

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.DoubleArgument
import dev.jorel.commandapi.arguments.EntitySelector
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.executors.CommandExecutor
import me.cobble.cocktail.utils.Strings
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Damageable
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

class DamageCommand {
    init {
        CommandAPICommand("damage")
            .withArguments(EntitySelectorArgument<Collection<Entity>>("entities", EntitySelector.MANY_ENTITIES))
            .withArguments(DoubleArgument("damage"))
            .executes(CommandExecutor { sender, args ->
                if (sender.isOp) {
                    if (args.size == 2) {
                        val selector = args[0] as ArrayList<*>
                        val damage = args[1] as Double

                        // loop through all entities, if they can be damaged, do so
                        selector.forEach { entity ->
                            if (entity is Damageable && entity is LivingEntity) {
                                if(damage < 0) {
                                    val maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
                                    val currentHealth = entity.health
                                    if((currentHealth + -damage) > maxHealth) entity.health = maxHealth
                                    else entity.health = entity.health + -damage
                                } else {
                                    entity.damage(damage)
                                }
                            }
                        }
                        return@CommandExecutor
                    } else {
                        sender.sendMessage("Too few arguments, /damage <selector> <amount>")
                        return@CommandExecutor
                    }
                } else {
                    sender.sendMessage(Strings.color("&cNo permission!"))
                    return@CommandExecutor
                }
            })
            .register()
    }
}
