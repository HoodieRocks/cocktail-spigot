package me.cobble.cocktail.utils

import me.cobble.cocktail.Cocktail
import org.bukkit.Bukkit
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Files
import java.time.Duration
import java.util.logging.Logger
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.system.measureTimeMillis

object DatapackUpdater {
  private val DATAPACK_PATH = "${Bukkit.getServer().getWorld("world")!!.worldFolder}/datapacks/"
  private val client = HttpClient
    .newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .followRedirects(HttpClient.Redirect.ALWAYS)
    .build()

  private lateinit var logger: Logger
  private var time: Long = 0
  private val dataFolder = Bukkit.getPluginManager().getPlugin("Cocktail")!!.dataFolder

  fun run(plugin: Cocktail) {
    logger = plugin.logger
    logger.info("Getting datapack list")
    val listOfUris = Config.getSection("datapack-urls").keys

    logger.info("Starting download and unzip process")
    listOfUris.forEach {
      val entry = Config.getSection("datapack-urls.$it")
      val uri = URI.create(entry.getString("url")!!)
      val request = HttpRequest.newBuilder()
        .uri(uri)
        .GET()
        .timeout(Duration.ofSeconds(30))
        .build()

      logger.info("Downloading ($it)...")
      time = measureTimeMillis {
        val stream = client.sendAsync(request, BodyHandlers.ofInputStream())
          .thenApply { obj: HttpResponse<InputStream> -> obj.body() }.join()

        BufferedOutputStream(FileOutputStream("$DATAPACK_PATH/${entry.nameAsString}.zip")).use { out ->
          stream.transferTo(out)
        }
      }
      logger.info("Download complete! (${time / 1000.0}s)")

      logger.info("Unzipping ($it)...")
      process(entry.nameAsString!!)
      logger.info("Done unzipping ($it)!")
    }

    Bukkit.getServer().reloadData()
  }

  /**
   * Processes a datapack
   */
  private fun process(name: String) {
    val zippedFile = "$DATAPACK_PATH/$name.zip"
    val destDir = File("$DATAPACK_PATH/$name-temp/")
    val zis = ZipInputStream(FileInputStream(zippedFile))
    var fixedFileName: String?

    time = measureTimeMillis {
      unzip(destDir, zis)
    }
    logger.info("Unzipping Complete! (${time / 1000.0}s)")

    zis.closeEntry()
    zis.close()

    logger.info("Beginning fixing of datapacks (if required)")

    time = measureTimeMillis {
      fixedFileName = fixZip(zippedFile, name, destDir)
    }

    moveResources(File("$DATAPACK_PATH/$fixedFileName"), name)

    logger.info("Fixed datapacks (${time / 1000.0}s)")
  }

  private fun newFile(destinationDir: File, zipEntry: ZipEntry): File {
    val destFile = File(destinationDir, zipEntry.name)
    val destDirPath = destinationDir.canonicalPath
    val destFilePath = destFile.canonicalPath

    if (!destFilePath.startsWith(destDirPath + File.separator)) {
      throw IOException("Entry is outside of the target dir: " + zipEntry.name)
    }
    return destFile
  }

  /**
   * Unzips the datapack
   */
  private fun unzip(destDir: File, zis: ZipInputStream) {
    val buffer = ByteArray(4096)
    var ze = zis.nextEntry

    while (ze != null) {
      val newFile: File = newFile(destDir, ze)
      if (ze.isDirectory) {
        if (!newFile.isDirectory && !newFile.mkdirs()) {
          throw IOException("Failed to create directory $newFile")
        }
      } else {
        // fix for Windows-created archives
        val parent = newFile.parentFile
        if (!parent.isDirectory && !parent.mkdirs()) {
          throw IOException("Failed to create directory $parent")
        }

        // write file content
        val fos = BufferedOutputStream(FileOutputStream(newFile))
        var len: Int
        while (zis.read(buffer).also { len = it } > 0) {
          fos.write(buffer, 0, len)
        }
        fos.close()
      }
      ze = zis.nextEntry
    }
  }

  /**
   * Fixes dually-nested data folders
   */
  private fun fixZip(file: String, name: String, destDir: File): String {
    val dirs = destDir.listFiles() ?: return ""
    var path: File? = null

    if (dirs.size == 1 && dirs[0].isDirectory) { // Zip contains folder with actual datapack

      path = dirs[0]
      val packPath = File("$DATAPACK_PATH/${path.name}")

      logger.info("Root folder contains folder with actual datapack... Fixing!")

      if (packPath.exists()) {
        packPath.deleteRecursively()
      }

      Files.move(path.toPath(), packPath.toPath())

      if (destDir.isDirectory && destDir.exists()) {
        destDir.deleteRecursively()
      }

      logger.info("Done!")
    }

    File(file).delete()
    return path?.name ?: name
  }

  private fun moveResources(packPath: File, name: String) {
    val dirs = packPath.listFiles() ?: return

    val resourcesFile = dirs.find { it.name.equals("resources.zip", true) }
    val packServerPath = File("$dataFolder/pack_server/$name-resources.zip")

    if (resourcesFile != null) {
      if (packServerPath.exists()) {
        packServerPath.delete()
      } else {
        packServerPath.mkdirs()
      }

      if (Files.exists(packServerPath.toPath())) Files.delete(packServerPath.toPath())
      Files.move(resourcesFile.toPath(), packServerPath.toPath())
    }
  }
}
