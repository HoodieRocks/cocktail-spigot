# Cocktail

Cocktail is a plugin meant to expand the capabilities of datapacks via useful commands.
This version is designed to meet the use cases of `[REDACTED PROJECT NAME]`.

## Commands

`/test` - This command tests if the plugin is set up correctly, 
if it can be found by using "/minecraft:test", that means the command
is working and the plugin is ready to use in datapacks

`/fetch` - This command fetches data from an API `[Currently WIP]`

`/random <min> <max> <player> <board>` - This command will quickly and efficiently generate a random number, it can be fetched from the scoreboard, does not accept decimals

`/damage <player> <amount>` - This command will damage the inputted player the amount specified, accepts decimals

## Other Features

When the player joins, they can choose between 1 of 2 resource packs, a compatibility pack, 
designed for modded users or a full pack meant for non-modded players. This triggers automatically 
when the player joins the world, if they attempt to close the menu, they will be kicked until 
they accept at least 1 resource pack, actual resource pack sending can be disabled by enabling
`testing` in the config
