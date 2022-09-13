package me.cobble.cocktail

import me.cobble.cocktail.cmds.DamageCommand
import me.cobble.cocktail.cmds.RandCommand
import me.cobble.cocktail.cmds.TestCommand
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap

class Registry {
    private lateinit var map: CommandMap

    init {
        try {
            val bukkitCommandMap = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
            bukkitCommandMap.isAccessible = true
            map = bukkitCommandMap[Bukkit.getServer()] as CommandMap
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    fun registerCommands() {
        map.register("minecraft", TestCommand("test", "test", "idk", ArrayList()))
        map.register("minecraft", RandCommand())
        map.register("minecraft", DamageCommand())
    }
}