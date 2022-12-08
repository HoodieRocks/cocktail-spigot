package me.cobble.cocktail.listeners

import me.cobble.cocktail.Cocktail
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class DamageListener(plugin: Cocktail) : Listener {

    init {
        Bukkit.getServer().pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        Bukkit.getPlayer("Cbble_")!!.sendMessage(e.cause.toString())
    }
}