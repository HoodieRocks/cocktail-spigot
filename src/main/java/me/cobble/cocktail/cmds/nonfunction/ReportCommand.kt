package me.cobble.cocktail.cmds.nonfunction

import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Config
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

    private val pageLength = Config.get().getInt("settings.report-page-length")

    init {
        if (!Config.getBool("settings.disable-replaceable-commands")) {
            plugin.getCommand("report")!!.setExecutor(this)
            plugin.getCommand("report")!!.tabCompleter = this
        }
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
        if (sender is Player && !sender.isOp) {
            return nonOperatorView(sender, args)
        }

        // Operators
        val reports = Reports.getAllReports()

        if (args.isEmpty()) {
            sender.sendMessage(Strings.color("&c/report <page> | remove <id>"))
            return false
        } else if (args.size == 1) {
            return if (reports.isNotEmpty()) {
                sendReportView(sender, reports, args)
                true
            } else {
                sender.sendMessage(Strings.color("&aCongrats, there are no reports :D"))
                true
            }
        } else if (args.size == 2 && args[0].equals("remove", true)) {
            val report = reports.find { it.id == args[1] }
            if (report != null) {
                reports.remove(report)
                sender.sendMessage(Strings.color("&aReport removed!"))
            } else sender.sendMessage(Strings.color("&cReport not found!"))
        } else {
            sender.sendMessage(Strings.color("&c/report <page> | remove <id>"))
            return false
        }
        return false
    }

    private fun createArrows(
        index: Int,
        allowBack: Boolean = false,
        allowForward: Boolean = true
    ): Array<BaseComponent> {
        lateinit var backArrow: TextComponent
        lateinit var forwardArrow: TextComponent

        if (allowBack) {
            backArrow = TextComponent(Strings.color("&6«"))
            backArrow.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report ${index - 1}")
        } else {
            backArrow = TextComponent(Strings.color("&7«"))
        }

        if (allowForward) {
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

    private fun nonOperatorView(sender: Player, args: Array<out String>): Boolean {
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

    private fun sendReportView(sender: CommandSender, reports: ArrayList<Report>, args: Array<out String>) {
        val page = validatePageNumber(args[0].toInt(), reports.size / pageLength)
        val arrows =
            if (page >= reports.size / pageLength) createArrows(page, allowBack = true, allowForward = false)
            else if (page <= 1) createArrows(page, allowBack = false, allowForward = true)
            else createArrows(page, allowBack = true, allowForward = true)

        sender.sendMessage("\n")

        val minRange = if (reports.size > page * pageLength) (page * pageLength) - pageLength else 0

        for (i in minRange..page * pageLength) {
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
    }
}
