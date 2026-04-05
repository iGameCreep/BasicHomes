# BasicHomes

**BasicHomes is an open-source plugin for managing homes and warps on your Minecraft server.**

***

### ✅ Compatible with Minecraft versions up to `26.1.1`

## 📜 Commands

#### Available commands :

*   `/config (get/set) (key) (value)` | Get or modify a config entry\*
*   `/home (name)` | Teleport to a home
*   `/homes` | View all your homes in a GUI
*   `/sethome (name)` | Create a home
*   `/delhome (name)` | Delete a home
*   `/edithome (name)` | Change the item of a home
*   `/delhomeof (player) (name)` | Delete someone's home\*
*   `/homesof (player)` | Show someone's homes in a GUI\*
*   `/warp (name)` | Teleport to a warp
*   `/warps` | View all warps in a GUI
*   `/setwarp` | Create a warp\*
*   `/delwarp` | Delete a warp\*
*   `/editwarp` | Change the item of a warp\*

_\* Requires permission not granted by default. OP players have access to all commands._ _You can use the command `/permissions` to edit these (see **Permissions** section below)._

## 🔐 Permissions

The command `/permissions` allows to grant/revoke access to a certain player, all players or OP players. Syntax: `/permissions <set|remove|list> <player|group> <name> <permission> <true|false>` Usages:

*   `/permissions set player Notch basichomes.warp.use false` (disallow player `Notch` from using warps)
*   `/permissions set group op basichomes.* true` (allow all OP players to have all permissions with the plugin)
*   `/permissions remove player Notch basichomes.warp.use` (back to default settings: `Notch` has no longer the permission to false)
*   `/permissions list Notch` (list all custom permissions given to `Notch`)

## 🐛 Found a Bug?

If you discover a bug or issue, please report it on our Discord server.
[https://discord.gg/VNXV4PDhfK](https://discord.gg/VNXV4PDhfK)