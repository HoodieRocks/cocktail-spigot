package me.cobble.cocktail.utils

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.cobble.cocktail.Cocktail
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

object Reports {

    private val allReports = arrayListOf<Report>()
    private const val idLength = 6
    private const val autoSaveTime: Long = 200

    fun createReport(
        sender: Player,
        player: Player,
        reason: String,
        time: LocalDateTime,
        id: String = Strings.randomBytes(idLength)
    ) {
        var safeID = id
        var i = 0
        var fails = 0
        while (i < allReports.size) {
            if (fails == 10) break
            else if (id == allReports[i].id) {
                safeID = Strings.randomBytes(idLength)
                i = 0
                fails++
            } else {
                i++
            }
        }

        if (fails == 10) {
            sender.sendMessage(Strings.color("&cWhoops! We've run into an issue, we've run out of report IDs!"))
            sender.sendMessage(Strings.color("&cPlease contact your server administrator"))
            return
        }

        allReports.add(Report(safeID, sender, player, reason, time))
    }

    fun getAllReports(): ArrayList<Report> = allReports

    fun save(plugin: Cocktail) {
        val gson = Gson()
        val jsonArray = JsonArray()
        val file = File("${plugin.dataFolder}/reports.json")

        if (!file.exists()) {
            file.createNewFile()
            val writer = FileWriter(file)
            gson.toJson(JsonArray(), writer)
            writer.flush()
            writer.close()
        }

        val writer = FileWriter(file)

        allReports.forEach {
            val jsonObject = JsonObject()
            jsonObject.addProperty("id", it.id)
            jsonObject.addProperty("time", it.time.toInstant(ZoneOffset.UTC).toEpochMilli())
            jsonObject.addProperty("senderUUID", it.sender.uniqueId.toString())
            jsonObject.addProperty("playerUUID", it.player.uniqueId.toString())
            jsonObject.addProperty("reason", it.reason)
            jsonArray.add(jsonObject)
        }

        gson.toJson(jsonArray, writer)
        writer.flush()
        writer.close()
    }

    fun load(plugin: Cocktail) {
        val gson = Gson()
        val file = File("${plugin.dataFolder}/reports.json")

        if (!file.exists()) {
            file.createNewFile()
            val writer = FileWriter("${plugin.dataFolder}/reports.json")
            gson.toJson(JsonArray(), writer)
            writer.flush()
            writer.close()
            return
        }

        val reader = FileReader(file)
        val jsonArray = gson.fromJson(reader, JsonArray::class.java)

        for (rawJsonObject in jsonArray) {
            val jsonObject = rawJsonObject.asJsonObject

            val id = jsonObject.get("id").asString
            val time = LocalDateTime.ofInstant(Instant.ofEpochMilli(jsonObject.get("time").asLong), ZoneOffset.UTC)
            val sender = Bukkit.getPlayer(UUID.fromString(jsonObject.get("senderUUID").asString))!!
            val player = Bukkit.getPlayer(UUID.fromString(jsonObject.get("playerUUID").asString))!!
            val reason = jsonObject.get("reason").asString

            createReport(sender, player, reason, time, id = id)
        }

        reader.close()

    }

    fun startAutoSave(plugin: Cocktail) {
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable { save(plugin) }, autoSaveTime, autoSaveTime)
    }
}
