package fr.gamecreep.basichomes.commands.utils;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.config.enums.ConfigElement;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.files.PositionDataHandler;
import fr.gamecreep.basichomes.utils.ChatUtils;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CreateCommand {
    private final BasicHomes plugin;
    private final PositionType type;
    private final Permission permission;
    private final PositionDataHandler handler;

    protected CreateCommand(@NonNull final BasicHomes plugin, final PositionType type, final Permission permission) {
        this.plugin = plugin;
        this.type = type;
        this.permission = permission;
        this.handler = plugin.getPositionDataHandler();
    }

    public boolean onCommand(@NonNull final CommandSender commandSender, @NonNull final String[] args) {
        if (commandSender instanceof final Player playerSender) {
            if (!playerSender.hasPermission(this.permission.getName())) {
                ChatUtils.sendNoPermission(playerSender, this.permission);
                return true;
            }

            if (args.length < 1) {
                ChatUtils.sendPlayerError(playerSender, "Please add a name !");
                return true;
            }

            final List<SavedPosition> list = this.handler.getAllByPlayer(this.type, playerSender);

            if (this.type.equals(PositionType.HOME) && !this.canCreateHome(playerSender, list.size())) {
                ChatUtils.sendPlayerError(playerSender, "You already have the max number of homes allowed !");
                return true;

            }

            final Location playerPos = playerSender.getLocation();
            final String name = args[0];
            Material material = null;

            if (args.length >= 2) {
                final String materialName = args[1].toUpperCase();
                try {
                    material = Material.valueOf(materialName);
                } catch (IllegalArgumentException e) {
                    ChatUtils.sendPlayerError(playerSender, String.format("Item '%s' doesn't exist.", materialName));
                    return true;
                }
            }

            for (final SavedPosition pos : list) {
                if (pos.getName().equalsIgnoreCase(name)) {
                    ChatUtils.sendPlayerError(playerSender, String.format("A %s with this name already exists !", this.type.getDisplayName()));
                    return true;
                }
            }

            SavedPosition toCreate;
            if (material != null) {
                toCreate = new SavedPosition(name, playerSender.getUniqueId().toString(), material, playerPos, this.type);
            } else {
                toCreate = new SavedPosition(name, playerSender.getUniqueId().toString(), playerPos, this.type);
            }

            this.handler.create(toCreate);
            ChatUtils.sendPlayerInfo(playerSender, String.format(
                    "The %s %s%s%s has been created !",
                    this.type.getDisplayName(),
                    Constants.SPECIAL_COLOR,
                    name,
                    Constants.SUCCESS_COLOR
            ));

            return true;
        }

        return false;
    }

    public List<String> onTabComplete(@NonNull final CommandSender commandSender, @NonNull final String[] args) {
        if (commandSender instanceof Player) {
            if (args.length == 1) return Collections.singletonList("[name]");
            if (args.length == 2) {
                final List<String> tabComplete = new ArrayList<>();
                for (final Material material : Material.values()) {
                    final String materialName = material.name().toLowerCase();
                    if (materialName.contains(args[1])) tabComplete.add(materialName);
                }
                return tabComplete;
            }
        }
        return Collections.emptyList();
    }

    private boolean canCreateHome(@NonNull final Player player, final int currentHomes) {
        final boolean opBypassLimit = (boolean) this.plugin.getPluginConfig().getConfig().get(ConfigElement.OP_BYPASS_HOME_LIMIT);
        if (opBypassLimit && player.hasPermission("op")) return true;

        final int maxHomes = (int) this.plugin.getPluginConfig().getConfig().get(ConfigElement.MAX_HOMES);
        if (maxHomes == 0) return true;

        return currentHomes < maxHomes;
    }
}
