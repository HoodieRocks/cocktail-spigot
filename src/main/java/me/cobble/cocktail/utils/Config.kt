package me.cobble.cocktail.utils

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

object Config {
    private val log = Bukkit.getLogger()
    private var file: File? = null
    private var yamlFile: YamlConfiguration? = null

    fun setup() {
        file = File(
            Bukkit.getServer().pluginManager.getPlugin("Cocktail")?.dataFolder,
            "config.yml"
        )
        if (!file!!.exists()) {
            log.info("No config found, Generating...")
            try {
                file!!.createNewFile()
            } catch (e: IOException) {
                // that's a lot of damage
                log.severe(e.message)
            }
        }
        yamlFile = YamlConfiguration.loadConfiguration(file!!)
    }

    fun get(): FileConfiguration = yamlFile!!

    fun getString(path: String): String = get().getString(path)!!

    fun getBool(path: String): Boolean = get().getBoolean(path)

    fun reload() { // NO_UCD (unused code)
        yamlFile = YamlConfiguration.loadConfiguration(file!!)
        log.info("Cocktail Config reloaded")
    }
}
