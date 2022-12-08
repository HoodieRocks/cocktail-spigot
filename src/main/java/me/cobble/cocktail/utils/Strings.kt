package me.cobble.cocktail.utils

import de.tr7zw.nbtapi.NBTType
import net.md_5.bungee.api.ChatColor

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

    fun getNBTTypeFromString(type: String, path: String): NBTType {
        return if (type.contains(' ') || type.contains("\"")) NBTType.NBTTagString
        else if (type.removeSuffix("s").toShortOrNull() != null && type.endsWith("s")) NBTType.NBTTagShort
        else if (type.removeSuffix("d")
                .toDoubleOrNull() != null && (!type.endsWith("f")) || type.endsWith("d")
        ) NBTType.NBTTagDouble
        else if (type.removeSuffix("f").toDoubleOrNull() != null && type.endsWith("f")) NBTType.NBTTagDouble
        else if (type.toIntOrNull() != null) NBTType.NBTTagInt
        else if (type.removeSuffix("l").toLongOrNull() != null && type.endsWith("l")) NBTType.NBTTagLong
        else if (type.removeSuffix("b").toByteOrNull() != null && type.endsWith("b")) NBTType.NBTTagByte
        else if (type.contains(Regex("(\\[[0-9]\\])+")) || path.contains(Regex("(\\[[0-9]\\])+"))) NBTType.NBTTagList
        else NBTType.NBTTagList
    }
}
