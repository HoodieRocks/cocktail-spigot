package me.cobble.cocktail.cmds

import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Report
import me.cobble.cocktail.utils.Reports
import me.cobble.cocktail.utils.Strings
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import java.time.LocalDateTime

class ReportCommand(plugin: Cocktail) : TabExecutor {

    init {
        // TODO: Rework reporting
        plugin.getCommand("report")!!.setExecutor(this)
        plugin.getCommand("report")!!.tabCompleter = this
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String> {
        val list = arrayListOf<String>()

        if (sender.isOp) {
            if (args.size == 1) {
                list.add("remove")
            }
            if (args.size == 2) {
                for (report in Reports.getAllReports()) {
                    list.add(report.id)
                }
            }
        }
        return list
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (!sender.isOp) {
                return if (args.size > 1) {
                    val target = Bukkit.getPlayer(args[0])!!
                    val reason = args.drop(1).joinToString(separator = " ")

                    Reports.createReport(sender, target, reason, LocalDateTime.now())

                    sender.sendMessage(Strings.color("&aReport sent! Thanks for reporting!"))
                    true
                } else {
                    sender.sendMessage(Strings.color("&c/report <player> <reason>"))
                    false
                }
            }

            // Operators
            val reports = Reports.getAllReports()

            if(args.isEmpty()) sender.sendMessage(Strings.color("&c/report <page> | remove <id>"))

            if (args.size == 1) {
                if (reports.isNotEmpty()) {
                    val page = validatePageNumber(args[0].toInt(), reports.size / 5)
                    val arrows =
                        if (page >= reports.size / 5) createArrows(page, backAllowed = true, forwardAllowed = false)
                        else if (page <= 1) createArrows(page, backAllowed = false, forwardAllowed = true)
                        else createArrows(page, backAllowed = true, forwardAllowed = true)

                    sender.sendMessage("\n")

                    for (i in (page * 5) - 5..page * 5) {
                        val report: Report =
                            if (page == 1) reports[i]
                            else reports[i - 1]

                        sender.sendMessage(
                            Strings.color(
                                "\n&eReport ID&7: &f${report.id}\n" +
                                        "&eReporter&7: &d${report.sender.name}\n" +
                                        "&ePlayer&7: &d${report.player.name}\n" +
                                        "&eReason&7: &f${report.reason}\n"
                            )
                        )
                    }

                    sender.spigot().sendMessage(*arrows)
                    sender.sendMessage("\n")
                    return true
                } else {
                    sender.sendMessage(Strings.color("&aCongrats, there are no reports :D"))
                    return true
                }
            }

            if(args.size == 2 && args[0].equals("remove", true)) {
                val report = reports.find { it.id == args[1] }
                if(report != null) {
                    reports.remove(report)
                    sender.sendMessage(Strings.color("&aReport removed!"))
                } else {
                    sender.sendMessage(Strings.color("&cReport not found!"))
                }
            } else {
                sender.sendMessage(Strings.color("&c/report <page> | remove <id>"))
                return false
            }
        } else {
            sender.sendMessage(Strings.color("&cThis can not be used by the console"))
            return false
        }
        return false
    }

    private fun createArrows(
        index: Int,
        backAllowed: Boolean = false,
        forwardAllowed: Boolean = true
    ): Array<BaseComponent> {
        lateinit var backArrow: TextComponent
        lateinit var forwardArrow: TextComponent

        if (backAllowed) {
            backArrow = TextComponent(Strings.color("&6«"))
            backArrow.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report ${index - 1}")
        } else {
            backArrow = TextComponent(Strings.color("&7«"))
        }

        if (forwardAllowed) {
            forwardArrow = TextComponent(Strings.color("&6»"))
            forwardArrow.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report ${index + 1}")
        } else {
            forwardArrow = TextComponent(Strings.color("&7»"))
        }

        return ComponentBuilder().append(backArrow).append(Strings.color("&7 | ")).append(forwardArrow).create()
    }

    private fun validatePageNumber(int: Int, max: Int): Int {
        var page = int
        if (page <= 0) {
            page = 1
        }
        if (page >= max) {
            page = max
        }
        return page
    }
}