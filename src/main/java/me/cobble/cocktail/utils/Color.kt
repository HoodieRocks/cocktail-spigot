package me.cobble.cocktail.utils

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.*

object Color {

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
                //get the next string
                i++
                if (texts[i][0] == '#') {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7)).toString() + texts[i].substring(7))
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]))
                }
            } else {
                finalText.append(texts[i])
            }
            i++
        }
        return finalText.toString()
    }


    fun colorToTextComponent(text: String): TextComponent {
        val texts: Array<String> =
            text.split(java.lang.String.format(WITH_DELIMITER, "&").toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val builder = ComponentBuilder()
        var i = 0
        while (i < texts.size) {
            val subComponent = TextComponent()
            if (texts[i].equals("&", ignoreCase = true)) {
                //get the next string
                i++
                if (texts[i][0] == '#') {
                    subComponent.text = texts[i].substring(7)
                    subComponent.color = ChatColor.of(texts[i].substring(0, 7))
                    builder.append(subComponent)
                } else {
                    if (texts[i].length > 1) {
                        subComponent.text = texts[i].substring(1)
                    } else {
                        subComponent.text = ""
                    }
                    when (texts[i][0]) {
                        '0' -> subComponent.color = ChatColor.BLACK
                        '1' -> subComponent.color = ChatColor.DARK_BLUE
                        '2' -> subComponent.color = ChatColor.DARK_GREEN
                        '3' -> subComponent.color = ChatColor.DARK_AQUA
                        '4' -> subComponent.color = ChatColor.DARK_RED
                        '5' -> subComponent.color = ChatColor.DARK_PURPLE
                        '6' -> subComponent.color = ChatColor.GOLD
                        '7' -> subComponent.color = ChatColor.GRAY
                        '8' -> subComponent.color = ChatColor.DARK_GRAY
                        '9' -> subComponent.color = ChatColor.BLUE
                        'a' -> subComponent.color = ChatColor.GREEN
                        'b' -> subComponent.color = ChatColor.AQUA
                        'c' -> subComponent.color = ChatColor.RED
                        'd' -> subComponent.color = ChatColor.LIGHT_PURPLE
                        'e' -> subComponent.color = ChatColor.YELLOW
                        'f' -> subComponent.color = ChatColor.WHITE
                        'k' -> subComponent.isObfuscated = true
                        'l' -> subComponent.isBold = true
                        'm' -> subComponent.isStrikethrough = true
                        'n' -> subComponent.isUnderlined = true
                        'o' -> subComponent.isItalic = true
                        'r' -> subComponent.color = ChatColor.RESET
                    }
                    builder.append(subComponent)
                }
            } else {
                builder.append(texts[i])
            }
            i++
        }
        return TextComponent(*builder.create())
    }
}