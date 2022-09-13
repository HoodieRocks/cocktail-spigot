package me.cobble.cocktail.utils

import org.bukkit.entity.Player
import java.time.LocalDateTime

data class Report(val player: Player, val reportedPlayer: Player, val reason: String, val reportTime: LocalDateTime)