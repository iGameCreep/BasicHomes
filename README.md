# BasicHomes

**BasicHomes is an open-source plugin for managing homes and warps on your Minecraft server.**

***

### ✅ Compatible with Minecraft `1.21.X` (1.21, 1.21.1, ..., 1.21.5)

## 📜 Commands

#### Available commands :

- `/config (get/set) (key) (value)` | Get or modify a config entry*
- `/home (name)` | Teleport to a home
- `/homes` | View all your homes in a GUI
- `/sethome (name)` | Create a home
- `/delhome (name)` | Delete a home
- `/edithome (name)` | Change the item of a home
- `/delhomeof (player) (name)` | Delete someone's home*
- `/homesof (player)` | Show someone's homes in a GUI*
- `/warp (name)` | Teleport to a warp
- `/warps` | View all warps in a GUI
- `/setwarp` | Create a warp*
- `/delwarp` | Delete a warp*
- `/editwarp` | Change the item of a warp* 

_\* Requires permission not granted by default. OP players have access to all commands._
_You can use the command `/permissions` to edit these (see **Permissions** section below)._

## ⚙️ Configuration

Here is the default `config.yml`:

```
# BasicHomes configuration file

homes:
  enabled: true
  delay: 0
  standStill: true

warps:
  enabled: true
  delay: 0
  standStill: true

op-bypass-home-limit: false
max-homes: 1
```

**Tips:**

*   `enabled`: Set to `false` to disable homes or warps.
*   `delay`: Time (in seconds) before teleportation. `0` disables delay and `standStill`.
*   `standStill`: Require players to remain still during teleportation.
*   `max-homes`: Maximum number of homes a player can have. Use `0` for no limit.
*   `op-bypass-home-limit`: Allow OPs to bypass the home limit.

## 🔐 Permissions

The command `/permissions` allows to grant/revoke access to a certain player, all players or OP players.
Syntax: `/permissions <set|remove|list> <player|group> <permission> <true|false>`
Usages:
- `/permissions set player Notch basichomes.warp.use false` (disallow player `Notch` from using warps)
- `/permissions set group op basichomes.* true` (allow all OP players to have all permissions with the plugin)
- `/permissions remove player Notch basichomes.warp.use` (back to default settings: `Notch` has no longer the permission to false)
- `/permissions list Notch` (list all custom permissions given to `Notch`)

### Available permissions :

- `basichomes.config` | Access to the **/config** command (OP)
- `basichomes.permissions` | Access to the **/permissions** command (OP)
- `basichomes.home.use` | Access to **/home**, **/sethome**, **/delhome**, **/homes** (default)
- `basichomes.home.manage` | Access to **/delhomeof**, **/homesof** (OP)
- `basichomes.warp.use` | Access to **/warp**, **/warps** (default)
- `basichomes.warp.create` | Access to **/setwarp** (OP)
- `basichomes.warp.delete` | Access to **/delwarp** (OP)
- `basichomes.home.*` | All home-related commands (**use**, **manage**) (OP)
- `basichomes.warp.*` | All warp-related commands (**use**, **create**, **delete**) (OP)
- `basichomes.*` | Full plugin access (OP)

## 🐛 Found a Bug?

If you discover a bug or issue, please report it on our Discord server.

## 🔗 Join the Community

**Discord:** [https://discord.gg/VNXV4PDhfK](https://discord.gg/VNXV4PDhfK)