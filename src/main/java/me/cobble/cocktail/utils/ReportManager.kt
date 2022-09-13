package me.cobble.cocktail.utils

import org.bukkit.entity.Player
import java.time.LocalDateTime

object ReportManager {

    private val reports: ArrayList<Report> = arrayListOf()

    fun createReport(sender: Player, reportedPlayer: Player, reason: String) {
        reports.add(Report(sender, reportedPlayer, reason, LocalDateTime.now()))
    }

    fun getAllReports(): ArrayList<Report> {
        return reports
    }
}