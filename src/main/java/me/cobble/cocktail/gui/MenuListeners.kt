package me.cobble.cocktail.gui

import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Config
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MenuListeners(plugin: Cocktail) : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onOpen(e: InventoryClickEvent) {
        val player = e.whoClicked as Player
        if (MenuManager.contains(player)) {
            e.isCancelled = true
            val item = e.currentItem!!
            if (item.type == Material.GRAY_DYE) {
                if (!Config.getBool("testing")) {
                    player.setResourcePack(Config.getString("compat-pack-url"))
                }
                MenuManager.remove(player)
                player.closeInventory()
            }
            if (item.type == Material.GREEN_DYE) {
                if (!Config.getBool("testing")) {
                    player.setResourcePack(Config.getString("full-pack-url"))
                }
                MenuManager.remove(player)
                player.closeInventory()
            }
        }
    }

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        if (MenuManager.contains(e.player as Player)) (e.player as Player).kickPlayer("Please accept one of the packs!")
    }
}