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

object ReportManager {

    private val reports = hashMapOf<UUID, Report>()

    fun createReport(sender: Player, reportedPlayer: Player, reason: String, time: LocalDateTime) {
        val id = UUID.randomUUID()
        reports[id] = Report(id, sender, reportedPlayer, reason, time)
    }

    private fun createReportWithSetUUID(
        uuid: UUID,
        sender: Player,
        reportedPlayer: Player,
        reason: String,
        time: LocalDateTime
    ) {
        reports[uuid] = Report(uuid, sender, reportedPlayer, reason, time)
    }

    fun getAllReports(): HashMap<UUID, Report> {
        return reports
    }

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

        for (reportEntry in reports) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("uuid", reportEntry.key.toString())
            jsonObject.addProperty("time", reportEntry.value.reportTime.toInstant(ZoneOffset.UTC).toEpochMilli())
            jsonObject.addProperty("reportUUID", reportEntry.value.player.uniqueId.toString())
            jsonObject.addProperty("reportedPlayerUUID", reportEntry.value.reportedPlayer.uniqueId.toString())
            jsonObject.addProperty("reason", reportEntry.value.reason)
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
            val uuid = jsonObject.get("uuid").asString
            createReportWithSetUUID(
                UUID.fromString(uuid),
                player,
                reportedPlayer,
                reason,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.UTC)
            )
        }

        reader.close()

    }
}