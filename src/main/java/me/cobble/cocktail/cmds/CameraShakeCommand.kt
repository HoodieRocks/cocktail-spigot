package me.cobble.cocktail.cmds

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import me.cobble.cocktail.Cocktail

class CameraShakeCommand(private val plugin: Cocktail) {
  init {
    commandAPICommand("camerashake") {

    }
  }
}
