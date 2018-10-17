# DropSpawner

**1.13 version coming when new documentation for Spigot 1.13 features comes out.**

Makes spawners drop when destroyed, simple.

All other plugins I've found that does this use their own usually buggy ways of setting spawner types. This plugin uses the native spigot functions to accomplish the same although this means only mobs that existed before 1.9 will work. This does however include all mobs from naturally generated spawners and a whole bunch more.

The other difference with this plugin is that it just does what it says it does, makes spawners drop when destroyed. Most other plugins like SilkSpawners have a whole bunch of other to me useless features that get in the way of the simple need to be able to pick up spawners.

By default the plugin will only drop spawners if the user is using a pickaxe with silk touch but this can be changed in the config.

## Config Options:

| Variable             | Default Value    | Description                                                                        |
| :------------------- |:-----------------| :----------------------------------------------------------------------------------|
| `require-pickaxe`    | `true`           | True if the player needs to use a pickaxe to get the drop from the spawner.        |
| `require-silktouch`  | `true`           | True if the player needs the silk touch enchantment to get drops from the spawner. |


## Permissions:

| Node                       | Default Value    | Description                                                                        |
| :--------------------------|:-----------------| :----------------------------------------------------------------------------------|
| `dropspawner.allowdrop`    | `true`           | Lets the player get drops when destroying spawners. Given to all by default.       |
