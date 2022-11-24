package me.cobble.cocktail.utils

import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player

object Strings {

    private const val WITH_DELIMITER = "((?<=%1\$s)|(?=%1\$s))"

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */
    fun color(text: String): String {
        val texts = text.split(String.format(WITH_DELIMITER, "&").toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val finalText = StringBuilder()
        var i = 0
        while (i < texts.size) {
            if (texts[i].equals("&", ignoreCase = true)) {
                // Get the next string
                i++
                if (texts[i][0] == '#') finalText.append(
                    ChatColor.of(texts[i].substring(0, 7)).toString() + texts[i].substring(7)
                )
                else finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]))
            } else {
                finalText.append(texts[i])
            }
            i++
        }
        return finalText.toString()
    }

    fun randomBytes(length: Int): String {
        val builder = StringBuilder()
        repeat(length) { builder.append((0x0..0xF).random().toString(16)) }
        return builder.toString()
    }

    fun Player.sendColoredMessage(s: String) {
        this.sendMessage(color(s))
    }
}
