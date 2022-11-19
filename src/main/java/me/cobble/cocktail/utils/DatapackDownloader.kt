package me.cobble.cocktail.utils

import org.bukkit.Bukkit
import java.io.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


object DatapackDownloader {
    private val DATAPACK_PATH = "${Bukkit.getServer().getWorld("world")!!.worldFolder}/datapacks/"

    private val client = HttpClient
        .newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build()

    fun getDatapacks() {
        val listOfUris = Config.get().getConfigurationSection("datapack-urls")!!.getKeys(false)

        listOfUris.forEach {
            val entry = Config.get().getConfigurationSection("datapack-urls.$it")!!
            val uri = URI.create(entry.getString("url")!!)
            val request = HttpRequest.newBuilder().uri(uri).build()
            val fileName = entry.name + ".zip"

            val stream = client.sendAsync(request, BodyHandlers.ofInputStream())
                .thenApply { obj: HttpResponse<InputStream> -> obj.body() }.join()

            FileOutputStream("$DATAPACK_PATH/$fileName").use { out -> stream.transferTo(out) }

            unzip(fileName)
        }

        Bukkit.getServer().reloadData()
    }

    private fun unzip(name: String) {

        val zippedFile = "$DATAPACK_PATH/$name"
        val noExtension = name.split(".")[0]
        val destDir = File("$DATAPACK_PATH/$noExtension-temp/")

        val buffer = ByteArray(4096)
        val zis = ZipInputStream(FileInputStream(zippedFile))
        var ze = zis.nextEntry // zip entry

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

        zis.closeEntry()
        zis.close()

        val dirs = destDir.listFiles()

        if (dirs != null) {
            if (dirs.size == 1 && dirs[0].isDirectory) { // Zip contains folder with actual datapack
                val path = dirs[0]
                val packPath = File("$DATAPACK_PATH/${path.name}")

                if (packPath.exists()) {
                    packPath.deleteRecursively()
                }

                Files.move(path.toPath(), packPath.toPath())

                if (destDir.isDirectory && destDir.exists()) {
                    destDir.deleteRecursively()
                }
            }

            File(zippedFile).delete()
        }
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
