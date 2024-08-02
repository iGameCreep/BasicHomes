package fr.gamecreep.basichomes.commands.config;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.config.enums.ConfigElement;
import fr.gamecreep.basichomes.config.enums.DataType;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.utils.ChatUtils;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ConfigCommand implements CommandExecutor, TabCompleter {
    private final BasicHomes plugin;

    public ConfigCommand(@NonNull final BasicHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull final CommandSender commandSender, @NonNull final Command command, @NonNull final String label, @NonNull final String[] args) {
        if (!(commandSender instanceof final Player playerSender)) {
            return false;
        }

        if (!playerSender.hasPermission(Permission.CONFIG.getName())) {
            ChatUtils.sendNoPermission(playerSender, Permission.CONFIG);
            return true;
        }

        if (args.length < 2) {
            return false;
        }

        ConfigElement selectedElement = null;
        for (ConfigElement element : ConfigElement.values()) {
            if (element.name().equalsIgnoreCase(args[1])) {
                selectedElement = element;
                break;
            }
        }

        if (selectedElement == null) return false;

        if (args[0].equalsIgnoreCase("get") && args.length == 2) {
            handleGetCommand(playerSender, selectedElement);
        } else if (args[0].equalsIgnoreCase("set") && args.length == 3) {
            handleSetCommand(playerSender, selectedElement, args[2]);
        }

        return true;
    }

    private void handleGetCommand(@NonNull final Player player, @NonNull final ConfigElement selectedElement) {
        final Object value = this.plugin.getPluginConfig().getConfig().get(selectedElement);
        ChatUtils.sendPlayerInfo(player, String.format(
                "Current value for %s%s%s is %s%s%s.",
                Constants.SPECIAL_COLOR,
                selectedElement.name(),
                Constants.SUCCESS_COLOR,
                Constants.SPECIAL_COLOR,
                value,
                Constants.SUCCESS_COLOR
        ));
    }

    private void handleSetCommand(@NonNull final Player player, @NonNull final ConfigElement selectedElement, final String newValue) {
        final Object currentValue = this.plugin.getPluginConfig().getConfig().get(selectedElement);
        final Object value = selectedElement.parseValue(newValue);
        if (value == null) {
            final String errMsg = "New value must be a number or a boolean (true|false) according to it's name. Check documentation for more information on this command.";
            ChatUtils.sendPlayerError(player, errMsg);
            return;
        }

        this.plugin.updateConfig(selectedElement, value);

        ChatUtils.sendPlayerInfo(player, String.format(
                "Successfully changed %s%s%s from %s%s%s to %s%s%s !",
                Constants.SPECIAL_COLOR,
                selectedElement.name(),
                Constants.SUCCESS_COLOR,
                Constants.SPECIAL_COLOR,
                currentValue,
                Constants.SUCCESS_COLOR,
                Constants.SPECIAL_COLOR,
                value,
                Constants.SUCCESS_COLOR
        ));
    }

    @Override
    public List<String> onTabComplete(@NonNull final CommandSender commandSender, @NonNull final Command command, @NonNull final String label, @NonNull final String[] args) {
        final List<String> options = new ArrayList<>();

        if (args.length == 1) {
            options.addAll(List.of("get", "set"));
        } else if (args.length == 2) {
            for (final ConfigElement element : ConfigElement.values()) {
                if (element.name().toLowerCase().contains(args[1])) {
                    options.add(element.name().toLowerCase());
                }
            }
        } else if (args.length == 3 && args[0].equals("set")) {
            final ConfigElement element = ConfigElement.valueOf(args[1].toUpperCase());

            if (element.getType().equals(DataType.BOOLEAN)) {
                options.addAll(List.of("true", "false"));
            } else if (element.getType().equals(DataType.INTEGER)) {
                options.add("[number]");
            }
        }

        return options;
    }
}
