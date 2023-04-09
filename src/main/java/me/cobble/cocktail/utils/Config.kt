package me.cobble.cocktail.utils

import dev.dejvokep.boostedyaml.YamlDocument
import dev.dejvokep.boostedyaml.block.implementation.Section
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings
import me.cobble.cocktail.Cocktail
import org.bukkit.Bukkit
import java.io.File

object Config {
  private val log = Bukkit.getLogger()
  private lateinit var yamlFile: YamlDocument

  fun setup(plugin: Cocktail) {
    yamlFile = YamlDocument.create(
      File(plugin.dataFolder, "config.yml"),
      plugin.getResource("config.yml")!!,
      GeneralSettings.DEFAULT,
      LoaderSettings.builder().setAutoUpdate(true).build(),
      DumperSettings.DEFAULT,
      UpdaterSettings
        .builder()
        .setVersioning(BasicVersioning("config-version"))
        .build(),
    )

    // create web server folders
  }

  fun get(): YamlDocument = yamlFile

  fun getSection(path: String): Section = get().getSection(path)

  fun getBool(path: String): Boolean = get().getBoolean(path)

  fun getInt(path: String): Int = get().getInt(path)

  fun reload() { // NO_UCD (unused code)
    yamlFile.reload()
    log.info("Cocktail Config reloaded")
  }
}
