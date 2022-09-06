package me.cobble.cocktail.cmds

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.querz.nbt.tag.CompoundTag
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.List

class FetchCommand : BukkitCommand("fetch", "fetches data using HTTP", "", List.of()) {
    private val client = HttpClient.newHttpClient()
    private val gson = Gson()
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
        if (args.size != 2) {
            try {
                // WTF
                val jsonData = client.send(
                    HttpRequest.newBuilder().uri(URI.create("API_NOT_DEFINED")).GET()
                        .header("accept", "application/json").build(), HttpResponse.BodyHandlers.ofString()
                ).body()
                val jsonObject = gson.fromJson(jsonData, JsonObject::class.java)
                val tag = CompoundTag()

                // Once API is finalized, then turn this into NBT conversion
            } catch (e: IOException) {
                throw RuntimeException(e)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
            return true
        }
        return false
    }
}