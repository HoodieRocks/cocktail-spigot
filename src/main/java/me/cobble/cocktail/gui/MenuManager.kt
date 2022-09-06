package me.cobble.cocktail.gui

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

object MenuManager {

    private val openMenus = hashMapOf<Player, Inventory>()

    fun contains(player: Player): Boolean {
        return openMenus.contains(player)
    }

    fun get(player: Player): Inventory {
        return openMenus[player]!!
    }

    fun add(player: Player, gui: Inventory) {
        openMenus[player] = gui
    }

    fun remove(player: Player) {
        openMenus.remove(player)
    }
}