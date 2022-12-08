package me.cobble.cocktail.cmds.function

import de.tr7zw.nbtapi.NBTContainer
import de.tr7zw.nbtapi.NBTEntity
import de.tr7zw.nbtapi.NBTType
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelector
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.arguments.GreedyStringArgument
import dev.jorel.commandapi.arguments.TextArgument
import dev.jorel.commandapi.executors.CommandExecutor
import me.cobble.cocktail.utils.Strings
import org.bukkit.entity.Player

class PDataCommand {

    init {
        CommandAPICommand("pdata")
            /*
            .withSubcommand(
                CommandAPICommand("get")
                    .withArguments(EntitySelectorArgument<Player>("player", EntitySelector.ONE_PLAYER))
                    .withArguments(StringArgument("path"))
                    .executes(CommandExecutor { sender, args ->
                        if (sender.isOp) {
                            val player = args[0] as Player
                            val nbtPlayer = NBTEntity(player)
                            val path = args[1] as String

                            when (nbtPlayer.getType(path)) {
                                NBTType.NBTTagByte -> {
                                    sender.sendMessage("Value of $path: ${nbtPlayer.getByte(path)} (byte/boolean)")
                                }
                                NBTType.NBTTagDouble -> {
                                    sender.sendMessage("Value of $path: ${nbtPlayer.getDouble(path)} (double)")
                                }
                                NBTType.NBTTagFloat -> {
                                    sender.sendMessage("Value of $path: ${nbtPlayer.getFloat(path)} (float)")
                                }
                                NBTType.NBTTagString -> {
                                    sender.sendMessage("Value of $path: ${nbtPlayer.getString(path)} (string)")
                                }
                                NBTType.NBTTagList -> {
                                    when(nbtPlayer.getListType(path)) {
                                        NBTType.NBTTagDouble -> {
                                            sender.sendMessage("Value of $path: ${nbtPlayer.getDoubleList(path)} (double list)")
                                        }
                                        NBTType.NBTTagFloat -> {
                                            sender.sendMessage("Value of $path: ${nbtPlayer.getFloatList(path)} (float list)")
                                        }
                                        NBTType.NBTTagString -> {
                                            sender.sendMessage("Value of $path: ${nbtPlayer.getStringList(path)} (string list)")
                                        }
                                        NBTType.NBTTagIntArray -> {
                                            sender.sendMessage("Value of $path: ${nbtPlayer.getIntArrayList(path)} (int list)")
                                        }
                                        NBTType.NBTTagLong -> {
                                            sender.sendMessage("Value of $path: ${nbtPlayer.getLongList(path)} (long list)")
                                        }
                                        NBTType.NBTTagInt -> {
                                            sender.sendMessage("Value of $path: ${nbtPlayer.getIntegerList(path)} (int list)")
                                        }
                                        else -> {
                                            sender.sendMessage("This type of list is not supported, contact cobble to fix!")
                                        }
                                    }
                                }
                                NBTType.NBTTagIntArray -> {
                                    sender.sendMessage("Value of $path: ${nbtPlayer.getIntArray(path)} (int[])")
                                }
                                NBTType.NBTTagShort -> {
                                    sender.sendMessage("Value of $path: ${nbtPlayer.getShort(path)} (short)")
                                }
                                NBTType.NBTTagLong -> {
                                    sender.sendMessage("Value of $path: ${nbtPlayer.getLong(path)} (long)")
                                }
                                NBTType.NBTTagInt -> {
                                    sender.sendMessage("Value of $path: ${nbtPlayer.getInteger(path)} (int)")
                                }
                                else -> {}
                            }
                        }
                    })
            )
            */
            .withSubcommand(
                CommandAPICommand("merge")
                    .withArguments(EntitySelectorArgument<Player>("player", EntitySelector.ONE_PLAYER))
                    .withArguments(GreedyStringArgument("nbt"))
                    .executes(CommandExecutor { sender, args ->
                        if (sender.isOp) {
                            val player = args[0] as Player
                            val nbt = args[1] as String
                            val nbtPlayer = NBTEntity(player)

                            nbtPlayer.mergeCompound(NBTContainer(nbt))
                            sender.sendMessage("Merged data!")
                        }
                    })
            )
            .withSubcommand(
                CommandAPICommand("set")
                    .withArguments(EntitySelectorArgument<Player>("player", EntitySelector.ONE_PLAYER))
                    .withArguments(TextArgument("path"))
                    .withArguments(GreedyStringArgument("value"))
                    .executes(CommandExecutor { sender, args ->
                        if (sender.isOp) {
                            val player = args[0] as Player
                            val path = args[1] as String
                            val value = args[2] as String
                            val valType = Strings.getNBTTypeFromString(value.trim(), path.trim())

                            val nbtPlayer = NBTEntity(player)

                            when (valType) {
                                NBTType.NBTTagByte -> {
                                    nbtPlayer.setByte(path, value.removeSuffix("b").toByteOrNull())
                                }

                                NBTType.NBTTagShort -> {
                                    nbtPlayer.setShort(path, value.removeSuffix("s").toShortOrNull())
                                }

                                NBTType.NBTTagLong -> {
                                    nbtPlayer.setLong(path, value.removeSuffix("l").toLongOrNull())
                                }

                                NBTType.NBTTagString -> {
                                    nbtPlayer.setString(path, value)
                                }

                                NBTType.NBTTagDouble -> {
                                    nbtPlayer.setDouble(path, value.removeSuffix("d").toDoubleOrNull())
                                }

                                NBTType.NBTTagFloat -> {
                                    nbtPlayer.setFloat(path, value.removeSuffix("f").toFloatOrNull())
                                }

                                NBTType.NBTTagInt -> {
                                    nbtPlayer.setInteger(path, value.toIntOrNull())
                                }

                                NBTType.NBTTagList -> {
                                    when (nbtPlayer.getListType(path)) {
                                        NBTType.NBTTagLong -> {
                                            nbtPlayer.setObject(
                                                path,
                                                value.replace("l", "").removeSurrounding("[", "]").split(",")
                                                    .map { it.toLong() })
                                        }

                                        NBTType.NBTTagString -> {
                                            nbtPlayer.setObject(path, value.removeSurrounding("[", "]").split(","))
                                        }

                                        NBTType.NBTTagDouble -> {
                                            nbtPlayer.setObject(
                                                path,
                                                value.replace("d", "").removeSurrounding("[", "]").split(",")
                                                    .map { it.toDouble() })
                                        }

                                        NBTType.NBTTagFloat -> {
                                            nbtPlayer.setObject(
                                                path,
                                                value.replace("f", "").removeSurrounding("[", "]").split(",")
                                                    .map { it.toFloat() })
                                        }

                                        NBTType.NBTTagInt -> {
                                            nbtPlayer.setObject(
                                                path,
                                                value.removeSurrounding("[", "]").split(",").map { it.toInt() })
                                        }

                                        else -> {
                                            sender.sendMessage("Unsupported type or invalid type declaration")
                                            println(nbtPlayer.getListType(path).name)
                                        }
                                    }
                                }

                                else -> {
                                    sender.sendMessage("Unsupported type or invalid type declaration")
                                    println(nbtPlayer.getListType(path).name)
                                    println(nbtPlayer.getType(path).name)
                                }
                            }
                        }
                    })
            )
            .withSubcommand(CommandAPICommand("remove"))
            .register()
    }
}