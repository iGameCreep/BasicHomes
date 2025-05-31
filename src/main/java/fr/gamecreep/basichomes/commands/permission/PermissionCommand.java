package fr.gamecreep.basichomes.commands.permission;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.permissions.DefaultPermissions;
import fr.gamecreep.basichomes.entities.permissions.PlayerPermissions;
import fr.gamecreep.basichomes.files.PermissionDataHandler;
import fr.gamecreep.basichomes.utils.ChatUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Command to handle plugin permission
 * Usage:
 *   /permissions set <player|group> <target> <permission> <true|false>
 *   /permissions remove <player|group> <target> <permission>
 *   /permissions list <player/group> <target>
 */
public class PermissionCommand implements CommandExecutor, TabCompleter {

    private final PermissionDataHandler permissionDataHandler;

    public PermissionCommand(@NonNull final BasicHomes plugin) {
        this.permissionDataHandler = plugin.getPermissionDataHandler();
    }

    @Override
    public boolean onCommand(@NonNull final CommandSender sender,
                             @NonNull final Command command,
                             @NonNull final String label,
                             @NonNull final String @NonNull[] args
    ) {
        if (args.length < 3) return false;

        if (!sender.hasPermission(Permission.PERMISSIONS.getName())) {
            ChatUtils.sendNoPermission(sender, Permission.PERMISSIONS);
            return true;
        }

        final String action = args[0].toLowerCase();
        final boolean isSet = action.equals(ActionType.SET.getStringValue());
        final boolean isRemove = action.equals(ActionType.REMOVE.getStringValue());

        if (action.equals(ActionType.LIST.getStringValue())) {
            this.handleListPermission(sender, args);
            return true;
        }

        if (!isSet && !isRemove) return false;
        if (args.length < 4) return false;
        if (isSet && args.length < 5) return false;

        final String targetType = args[1].toLowerCase();
        final String targetName = args[2];
        final String permission = args[3].toLowerCase();

        if (targetType.equals(TargetType.PLAYER.getStringValue())) {
            this.handlePlayerPermission(
                    sender,
                    targetName,
                    permission,
                    isSet ? ActionType.SET : ActionType.REMOVE,
                    isSet ? args[4] : null
            );
            return true;
        }

        if (targetType.equals(TargetType.GROUP.getStringValue())) {
            this.handleGroupPermission(
                    sender,
                    targetName,
                    permission,
                    isSet ? ActionType.SET : ActionType.REMOVE,
                    isSet ? args[4] : null
            );
            return true;
        }

        return false;
    }

    private void handlePlayerPermission(
            @NonNull final CommandSender sender,
            @NonNull final String targetName,
            @NonNull final String permission,
            @NonNull final ActionType actionType,
            @Nullable final String value
    ) {
        final Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            ChatUtils.sendPlayerError(sender, "Player not found: " + targetName);
            return;
        }

        if (actionType.equals(ActionType.SET)) {
            final Boolean boolValue = this.parseBooleanSafe(value);
            if (boolValue == null) {
                ChatUtils.sendPlayerError(sender, "Invalid value. Use 'true' or 'false'.");
                return;
            }

            this.permissionDataHandler.setPermission(target.getUniqueId(), permission, boolValue);
            this.sendInfo(sender, permission, boolValue, TargetType.PLAYER, target.getName());
        } else {
            this.permissionDataHandler.removePermission(target.getUniqueId(), permission);
            this.sendRemoveInfo(sender, permission, TargetType.PLAYER, target.getName());
        }
    }

    private void handleGroupPermission(
            @NonNull final CommandSender sender,
            @NonNull final String groupName,
            @NonNull final String permission,
            @NonNull final ActionType actionType,
            @Nullable final String value) {
        DefaultPermissions.GroupPermission group;
        try {
            group = DefaultPermissions.GroupPermission.valueOf(groupName.toUpperCase());
        } catch (IllegalArgumentException e) {
            ChatUtils.sendPlayerError(sender, "Invalid group. Use 'all' or 'op'.");
            return;
        }

        if (actionType.equals(ActionType.SET)) {
            final Boolean boolValue = this.parseBooleanSafe(value);
            if (boolValue == null) {
                ChatUtils.sendPlayerError(sender, "Invalid value. Use 'true' or 'false'.");
                return;
            }

            this.permissionDataHandler.setDefaultPermission(group, permission, boolValue);
            this.sendInfo(sender, permission, boolValue, TargetType.GROUP, group.name().toLowerCase());
        } else {
            this.permissionDataHandler.removeDefaultPermission(group, permission);
            this.sendRemoveInfo(sender, permission, TargetType.GROUP, group.name().toLowerCase());
        }
    }

    private void handleListPermission(@NonNull final CommandSender sender, @NonNull final String @NonNull[] args) {
        final String targetType = args[1].toLowerCase();
        final String targetName = args[2];

        Map<String, Boolean> permissions = new HashMap<>();

        if (targetType.equalsIgnoreCase(TargetType.PLAYER.getStringValue())) {
            final Player target = Bukkit.getPlayer(targetName);
            if (target == null) {
                ChatUtils.sendPlayerError(sender, "Player not found: " + targetName);
                return;
            }

            for (final PlayerPermissions playerPermissions : this.permissionDataHandler.getPlayerPermissions()) {
                if (playerPermissions.getPlayerId().equals(target.getUniqueId())) {
                    permissions.putAll(playerPermissions.getPermissions());
                }
            }
        } else {
            DefaultPermissions.GroupPermission group;
            try {
                group = DefaultPermissions.GroupPermission.valueOf(targetName.toUpperCase());
            } catch (IllegalArgumentException e) {
                ChatUtils.sendPlayerError(sender, "Invalid group. Use 'all' or 'op'.");
                return;
            }

            for (final DefaultPermissions defaultPermissions : this.permissionDataHandler.getDefaultPermissions()) {
                if (defaultPermissions.getGroup().equals(group)) {
                    permissions.putAll(defaultPermissions.getPermissions());
                }
            }
        }

        if (permissions.isEmpty()) {
            ChatUtils.sendPlayerInfo(
                    sender,
                    "No custom permissions found for "
                            + targetType + " "
                            + Constants.SPECIAL_COLOR + targetName
                            + Constants.SUCCESS_COLOR + "."
            );
        } else {
            ChatUtils.sendPlayerInfo(
                    sender,
                    "Custom permissions of "
                            + targetType + " "
                            + Constants.SPECIAL_COLOR + targetName
                            + Constants.SUCCESS_COLOR + ":"
            );
            permissions.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        final ChatColor valueColor = Boolean.TRUE.equals(entry.getValue()) ? ChatColor.GREEN : ChatColor.RED;
                        ChatUtils.sendPlayerInfo(
                                sender,
                                ChatColor.GRAY + "- "
                                        + ChatColor.WHITE + entry.getKey()
                                        + ChatColor.GRAY + ": "
                                        + valueColor + entry.getValue());
                    });
        }
    }


    @Nullable
    private Boolean parseBooleanSafe(final @Nullable String input) {
        if (input == null) return null;
        if (input.equalsIgnoreCase("true")) return Boolean.TRUE;
        if (input.equalsIgnoreCase("false")) return Boolean.FALSE;
        return null;
    }

    private void sendInfo(@NonNull final CommandSender receiver,
                          @NonNull final String permission,
                          final boolean value,
                          @NonNull final TargetType targetType,
                          @NonNull final String target
    ) {
        ChatUtils.sendPlayerInfo(receiver, String.format(
                "Permission §e%s§a set to §e%s§a for %s §e%s§a.",
                permission, value, targetType.getStringValue(), target
        ));
    }

    private void sendRemoveInfo(@NonNull final CommandSender receiver,
                                @NonNull final String permission,
                                @NonNull final TargetType targetType,
                                @NonNull final String target
    ) {
        ChatUtils.sendPlayerInfo(receiver, String.format(
                "Permission §e%s§a removed for %s §e%s§a.",
                permission, targetType.getStringValue(), target
        ));
    }

    @Override
    public List<String> onTabComplete(@NonNull final CommandSender sender,
                                      @NonNull final Command command,
                                      @NonNull final String label,
                                      @NonNull final String @NonNull[] args) {
        final String input = args[args.length - 1].toLowerCase();

        if (args.length == 1) {
            return filterContains(List.of(
                    ActionType.SET.getStringValue(),
                    ActionType.REMOVE.getStringValue(),
                    ActionType.LIST.getStringValue()
            ), input);
        } else if (args.length == 2) {
            return filterContains(List.of(
                    TargetType.PLAYER.getStringValue(),
                    TargetType.GROUP.getStringValue()
            ), input);
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase(TargetType.PLAYER.getStringValue())) {
                return filterContains(
                        Bukkit.getServer().getOnlinePlayers().stream()
                                .map(Player::getName)
                                .toList(),
                        input
                );
            } else {
                return filterContains(List.of(
                        DefaultPermissions.GroupPermission.ALL.getValueString(),
                        DefaultPermissions.GroupPermission.OP.getValueString()
                ), input);
            }
        } else if (args.length == 4 && !args[0].equals(ActionType.LIST.getStringValue())) {
            return filterContains(
                    Arrays.stream(Permission.values())
                            .map(Permission::getName)
                            .toList(),
                    input
            );
        } else if (args.length == 5 && args[0].equals(ActionType.SET.getStringValue())) {
            return filterContains(List.of("true", "false"), input);
        }

        return Collections.emptyList();
    }

    private List<String> filterContains(@NonNull final List<String> options, @NonNull final String input) {
        return options.stream()
                .filter(option -> option.toLowerCase().contains(input))
                .toList();
    }

    @Getter
    @RequiredArgsConstructor
    private enum ActionType {
        SET("set"),
        REMOVE("remove"),
        LIST("list");

        private final String stringValue;
    }

    @Getter
    @RequiredArgsConstructor
    private enum TargetType {
        PLAYER("player"),
        GROUP("group");

        private final String stringValue;
    }
}
