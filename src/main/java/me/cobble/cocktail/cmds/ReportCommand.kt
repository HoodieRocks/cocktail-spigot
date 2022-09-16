package me.cobble.cocktail.cmds

import me.cobble.cocktail.Cocktail
import me.cobble.cocktail.utils.Color
import me.cobble.cocktail.utils.ReportManager
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.util.*

class ReportCommand(plugin: Cocktail) : TabExecutor {

    init {
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

        if(sender.isOp) {
            if (args.size == 1) {
                list.add("remove")
            }
            if(args.size == 2) {
                for (report in ReportManager.getAllReports()) {
                    list.add(report.key.toString())
                }
            }
        }
        return list
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (!sender.isOp) {
                if (args.size > 1) {
                    val target = Bukkit.getPlayer(args[0])!!
                    val reason = args.drop(1).joinToString(separator = " ")

                    ReportManager.createReport(sender, target, reason, LocalDateTime.now())

                    sender.sendMessage(Color.color("&aReport sent! Thanks for reporting!"))
                    return true
                } else {
                    sender.sendMessage(Color.color("&c/report <player> <reason>"))
                }
            } else {
                val reports = ReportManager.getAllReports()
                val component = ComponentBuilder()

                val backComponent: TextComponent?
                val forwardComponent: TextComponent?

                if(args.size == 1) {
                    if (args[0] != "0") {
                        backComponent = TextComponent(Color.color("&6«"))
                        backComponent.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report ${args[0].toInt() - 1}")
                    } else {
                        backComponent = TextComponent(Color.color("&7«"))
                    }

                    if (args[0].toInt() > reports.size / 10) {
                        forwardComponent = TextComponent(Color.color("&7»"))
                    } else {
                        forwardComponent = TextComponent(Color.color("&6»"))
                        forwardComponent.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report ${args[0].toInt() + 1}")
                    }

                    component.append(backComponent)
                    component.append(Color.color("&7 | "))
                    component.append(forwardComponent)

                    sender.sendMessage(Color.color("\n&e&lReports &fPage ${args[0]} of ${(reports.size / 10) + 1}"))
                    for (i in args[0].toInt() until 10 * args[0].toInt()) {
                        if(reports.size == i) break
                        val report = reports.values.random()
                        sender.sendMessage(Color.color("&7---"))
                        sender.sendMessage(Color.color("&eID: &f${report.id}"))
                        sender.sendMessage(Color.color("&eSender: &f${report.player.name}"))
                        sender.sendMessage(Color.color("&eReason: &d${report.reportedPlayer.name}&f reported for ${report.reason} at &7${report.reportTime}\n"))
                    }
                    sender.spigot().sendMessage(*component.create())
                } else {
                    backComponent = TextComponent(Color.color("&7«"))

                    if (reports.size < 10) {
                        forwardComponent = TextComponent(Color.color("&7»"))
                    } else {
                        forwardComponent = TextComponent(Color.color("&6»"))
                        forwardComponent.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report 2")
                    }

                    component.append(backComponent)
                    component.append(Color.color("&7 | "))
                    component.append(forwardComponent)

                    sender.sendMessage(Color.color("&e&lReports &fPage 1 of ${(reports.size / 10) + 1}"))
                    for (i in 0 until 10) {
                        if(reports.size == i) break
                        val report = reports.values.random()
                        sender.sendMessage(Color.color("&7---"))
                        sender.sendMessage(Color.color("&eID: &f${report.id}"))
                        sender.sendMessage(Color.color("&eSender: &f${report.player.name}"))
                        sender.sendMessage(Color.color("&eReason: &d${report.reportedPlayer.name} &7reported for &f${report.reason} &7at &d${report.reportTime}\n"))
                    }
                    sender.spigot().sendMessage(*component.create())
                }
                return true
            }
        } else {
            sender.sendMessage(Color.color("&cThis can not be used by the console"))
            return false
        }
        return false
    }
}