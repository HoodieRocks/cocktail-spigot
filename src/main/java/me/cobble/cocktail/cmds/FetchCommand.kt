package me.cobble.cocktail.cmds

import com.google.gson.Gson
import com.google.gson.JsonObject
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.DoubleArgument
import dev.jorel.commandapi.arguments.EntitySelector
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.executors.CommandExecutor
import net.querz.nbt.tag.CompoundTag
import org.bukkit.entity.Player
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

// TODO fix when API is available
class FetchCommand {
    private val client = HttpClient.newHttpClient()
    private val gson = Gson()

    init {
        CommandAPICommand("fetch")
            .withArguments(EntitySelectorArgument<Player>("player", EntitySelector.ONE_PLAYER))
            .withArguments(DoubleArgument("damage"))
            .executes(CommandExecutor { sender, args ->
                if (sender.isOp && args.size != 2) {
                    try {
                        // WTF
                        val jsonData = client.send(
                            HttpRequest.newBuilder().uri(URI.create("API_NOT_DEFINED")).GET()
                                .header("accept", "application/json").build(), HttpResponse.BodyHandlers.ofString()
                        ).body()
                        val jsonObject = gson.fromJson(jsonData, JsonObject::class.java)
                        val tag = CompoundTag()

                        // TODO Once API is finalized, then turn this into NBT conversion
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    } catch (e: InterruptedException) {
                        throw RuntimeException(e)
                    }
                    return@CommandExecutor
                }
                return@CommandExecutor
            })
    }
}