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

## Commands

Here are all the permission available at the moment ! (v1.7 or newer)
To give specific permissions to players, you will need a plugin like [LuckPerms](https://luckperms.net/download) or [Vault](https://dev.bukkit.org/projects/vault).
With one, you can grant the permissions needed to players to give them access to commands ! Use the spreadsheet to find the permission you want.

| Permission             | Usage                             | Description                                     | Default |
|------------------------|-----------------------------------|-------------------------------------------------|---------|
| basichomes.home.use    | /home, /sethome, /delhome, /homes | Allows to use any home command (for users)      | true    |
| basichomes.home.manage | /delhomeof, /homesof              | Allows to use any admin home command            | OP      |
| basichomes.warp.use    | /warp, /homes                     | Allows to use any warp command (for users)      | true    |
| basichomes.warp.create | /setwarp                          | Allows to use /setwarp command (create a warp)  | OP      |
| basichomes.warp.delete | /delwarp                          | Allows to use /delwarp command (delete a warp)  | OP      |
| basichomes.home.*      | All home commands (user+admin)    | Allows to use any home command (user+admin)     | OP      |
| basichomes.warp.*      | All warp commands (user+admin)    | Allows to use any warp command (user+admin)     | OP      |
| basichomes.*           | All of the above                  | Allows to use any command (wildcard permission) | OP      |