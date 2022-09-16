package me.cobble.cocktail.utils

import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.util.UUID

data class Report(val id: UUID, val player: Player, val reportedPlayer: Player, val reason: String, val reportTime: LocalDateTime)