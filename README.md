![cocktail-glass_1f378](https://user-images.githubusercontent.com/62707056/198886948-4f700ee2-fa48-405d-a5ea-443aa14bbb9b.png)

# Cocktail

Cocktail is a plugin meant to expand the capabilities of datapacks via useful commands.
This version is designed to meet the use cases of `[REDACTED PROJECT NAME]`.

## Commands

* `/test` - This command tests if the plugin is set up correctly, if it can be found by using "/minecraft:test", that
  means the command is working and the plugin is ready to use in datapacks
* `/ctversion` - Says you the current version of cocktail on the server.
* `/fetch` - This command fetches data from an API `[Currently WIP] [Removed until API comes]`
* `/rand <min> <max>` - This command will quickly and efficiently generate a random number, it's value can be fetched
  with `/execute store`
* `/damage <selector> <amount>` - This command will damage the entities that match the selector by the amount specified,
  accepts decimals
* `/flyspeed <number>` - Will set your flying speed (maximum speed is 10) (limited by spigot/paper)
* `/timer <time> <selector> <function>` - Runs function asynchronously after `time` number of ticks
* `/velocity aligned|relative <x> <y> <z>` - Sets your velocity, relative is relative to you, while aligned is relative
  to Minecraft's axis
* `/velocity scoreboard <objective>` - Sets your velocity using values from a scoreboard, must have x, y, and z fake
  players (case-sensitive)
* `/report <player> <reasons>` - Reports a player (non-op)
* `/report [page]` - Shows the reports (ops)
* `/report remove <id>` - Removes report (ops)
