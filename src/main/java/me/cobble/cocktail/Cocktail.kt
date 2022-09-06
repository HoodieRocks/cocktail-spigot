package me.cobble.cocktail

import me.cobble.cocktail.gui.MenuListeners
import me.cobble.cocktail.listeners.ResourcesEvents
import me.cobble.cocktail.utils.Config
import me.cobble.cocktail.utils.HTTPUtils
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class Cocktail : JavaPlugin() {
    private lateinit var registry: Registry

    override fun onEnable() {
        // Plugin startup logic
        registry = Registry()

        this.saveDefaultConfig()
        Config.setup()

        HTTPUtils.getDatapacks()
        fixZip()

        ResourcesEvents(this)
        MenuListeners(this)

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun fixZip() {
        val fileZip = "${HTTPUtils.PATH}/pack.zip"
        val destDir = File("${HTTPUtils.PATH}/pack-test/")
        val buffer = ByteArray(1024)
        val zis = ZipInputStream(FileInputStream(fileZip))
        var zipEntry = zis.nextEntry
        while (zipEntry != null) {
            val newFile: File = newFile(destDir, zipEntry)
            if (zipEntry.isDirectory) {
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
                val fos = FileOutputStream(newFile)
                var len: Int
                while (zis.read(buffer).also { len = it } > 0) {
                    fos.write(buffer, 0, len)
                }
                fos.close()
            }
            zipEntry = zis.nextEntry
        }

        zis.closeEntry()
        zis.close()

        val movedFolder = File(destDir, "/datapack-main")
        movedFolder.renameTo(File(destDir, "../pack"))
        File(fileZip).delete()
        destDir.delete()
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
}