# BasicHomes
## A basic plugin to manage your homes AND warps in your Minecraft server !

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)

## Requirements
- **Java** and **Maven** to build the .jar file

## Installing

- Clone the repository
- Compile the project with `mvn package` command and the **.jar** file of the plugin will appear in the **target** folder !

## Commands

Here are all the commands available at the moment ! (v1.7 or newer)

| Command    | Usage                      | Description                                   |
|------------|----------------------------|-----------------------------------------------|
| /home      | /home (name)               | Teleport to a home by it's name !             |
| /homes     | /homes                     | Get all of your homes in an inventory !       |
| /sethome   | /sethome (name)            | Create a home and give it a name !            |
| /delhome   | /delhome (name)            | Delete a home by it's name !                  |
| /delhomeof | /delhomeof <player> (name) | Delete the home of someone !*                 |
| /homesof   | /homesof <player> (name)   | Access the homes of a player !*               |
| /warp      | /warp (name)               | Teleport to a warp by it's name !             |
| /warps     | /warps                     | Get all of the server warps in an inventory ! |
| /setwarp   | /setwarp (name)            | Create a warp and give it a name !*           |
| /delwarp   | /delwarp (name)            | Delete a warp by it's name !                  |

*Requires a permission not given by default (all permissions are granted to OP players)

## Config file

Here is the default config file:
```
# Config file for BasicHomes.
# To enable/disable homes/warps, set the enabled value to true or false.
# To wait before teleporting to a home/warp, set the delay value to
# the number of seconds to wait or 0 to teleport directly.
# To require the player to stand still while the plugin
# is teleporting them, set the standStill value to true, else false.

homes:
  enabled: true
  delay: 0
  standStill: true

warps:
  enabled: true
  delay: 0
  standStill: true
```
You can change the values from `true` to `false` to enable/disable the homes and warps in your server or enable/disable standing still while waiting to be teleported.
Setting the `delay` value to `0` will by default disable it and disable the `standStill` option too. 

## Permissions

Here are all the permission available at the moment ! (v1.7 or newer)
To give specific permissions to players, you will need a plugin like [LuckPerms](https://luckperms.net/download) or [Vault](https://dev.bukkit.org/projects/vault).
With one, you can grant the permissions needed to players to give them access to commands ! Use the spreadsheet to find the permission you want.

| Permission             | Usage                              | Default |
|------------------------|------------------------------------|---------|
| basichomes.home.use    | /home, /sethome, /delhome, /homes  | true    |
| basichomes.home.manage | /delhomeof, /homesof               | OP      |
| basichomes.warp.use    | /warp, /homes                      | true    |
| basichomes.warp.create | /setwarp                           | OP      |
| basichomes.warp.delete | /delwarp                           | OP      |
| basichomes.home.*      | All home commands (user+admin)     | OP      |
| basichomes.warp.*      | All warp commands (user+admin)     | OP      |
| basichomes.*           | All of the above                   | OP      |