package me.cobble.cocktail.gui

import me.cobble.cocktail.utils.Color
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object ResourcesMenuGUI {
    private lateinit var inv: Inventory

    fun open(player: Player): Inventory {
        inv = Bukkit.getServer().createInventory(player, 27, "Choose a pack type")

        MenuManager.add(player, inv)

        val compatPackItem = ItemStack(Material.GRAY_DYE, 1)
        val compatItemMeta = compatPackItem.itemMeta!!

        compatItemMeta.setDisplayName(Color.color("&aCompat pack"))

        compatItemMeta.lore = listOf(
            "",
            "This pack is for modded users!",
            "It does not contain features that may",
            "break in modded scenarios.",
            "This pack is safe for modded environments",
            Color.color("&aClick me to use the Compatibility Pack!"))
        compatPackItem.itemMeta = compatItemMeta

        val fullPackItem = ItemStack(Material.GREEN_DYE, 1)
        val fullItemMeta = fullPackItem.itemMeta!!

        fullItemMeta.setDisplayName(Color.color("&dFull Pack"))

        fullItemMeta.lore = listOf(
            "",
            "This pack is for Vanilla users!",
            "It contains all the feature we provide.",
            "These may break in modded scenario!",
            Color.color("&aClick me to use the Full Pack!"))
        fullPackItem.itemMeta = fullItemMeta

        inv.setItem(10, compatPackItem)
        inv.setItem(16, fullPackItem)

        return inv
    }
}