package me.cobble.cocktail.utils

import org.bukkit.Bukkit
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers


object HTTPUtils {
    private val client = HttpClient
        .newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build()

    fun getDatapacks() {
        val uri = URI.create(Config.get()!!.getString("datapack-url")!!)
        val request = HttpRequest.newBuilder().uri(uri).build()

        val stream = client.sendAsync(request, BodyHandlers.ofInputStream())
            .thenApply { obj: HttpResponse<InputStream> -> obj.body() }.join()
        FileOutputStream("${Bukkit.getServer().getWorld("world")!!.worldFolder}/datapacks/pack.zip")
            .use { out -> stream.transferTo(out) }
    }
}