package me.cobble.cocktail.utils

import org.bukkit.entity.Player
import java.time.LocalDateTime

data class Report(
    val id: String,
    val sender: Player,
    val player: Player,
    val reason: String,
    val time: LocalDateTime
)