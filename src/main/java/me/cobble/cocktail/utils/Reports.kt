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

    private val reports = arrayListOf<Report>()

    private fun createReportWithSpecifiedId(
        id: String,
        sender: Player,
        reportedPlayer: Player,
        reason: String,
        time: LocalDateTime
    ) {
        reports.add(Report(id, sender, reportedPlayer, reason, time))
    }

    fun createReport(sender: Player, reportedPlayer: Player, reason: String, time: LocalDateTime) {
        reports.add(Report(Strings.randomString(8), sender, reportedPlayer, reason, time))
    }

    fun getAllReports(): ArrayList<Report> = reports

    fun save(plugin: Cocktail) {
        val gson = Gson()
        val jsonArray = JsonArray()
        val file = File("${plugin.dataFolder}/reports.json")

        if (!file.exists()) {
            file.createNewFile()
            val writer = FileWriter("${plugin.dataFolder}/reports.json")
            gson.toJson(JsonArray(), writer)
            writer.flush()
            writer.close()
            return
        }

        val writer = FileWriter(file)

        reports.forEach {
            val jsonObject = JsonObject()
            jsonObject.addProperty("id", it.id)
            jsonObject.addProperty("time", it.time.toInstant(ZoneOffset.UTC).toEpochMilli())
            jsonObject.addProperty("reportUUID", it.sender.uniqueId.toString())
            jsonObject.addProperty("reportedPlayerUUID", it.player.uniqueId.toString())
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
            val time = jsonObject.get("time").asLong
            val player = Bukkit.getPlayer(UUID.fromString(jsonObject.get("reportUUID").asString))!!
            val reportedPlayer = Bukkit.getPlayer(UUID.fromString(jsonObject.get("reportedPlayerUUID").asString))!!
            val reason = jsonObject.get("reason").asString
            val id = jsonObject.get("id").asString
            createReportWithSpecifiedId(
                id,
                player,
                reportedPlayer,
                reason,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.UTC)
            )
        }

        reader.close()

    }
}