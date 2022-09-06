package me.cobble.cocktail.listeners

import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.gui.ResourcesMenuGUI
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent

class ResourcesEvents(plugin: Cocktail) : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player

        player.openInventory(ResourcesMenuGUI.open(player))
    }

    @EventHandler
    fun checkResourceStatus(e: PlayerResourcePackStatusEvent) {
        if (e.status == PlayerResourcePackStatusEvent.Status.DECLINED) e.player.kickPlayer("Sorry, but resource packs are required to join")
    }
}