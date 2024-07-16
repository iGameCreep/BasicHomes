package fr.gamecreep.basichomes.commands.utils;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.files.PositionDataHandler;
import fr.gamecreep.basichomes.utils.ChatUtils;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class EditCommand {
    private final PositionType type;
    private final Permission permission;
    private final PositionDataHandler handler;

    protected EditCommand(@NonNull final BasicHomes plugin, final PositionType type, final Permission permission) {
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

            if (args.length < 2) {
                ChatUtils.sendPlayerError(playerSender, String.format("This command requires a %s and an item !", this.type.getDisplayName()));
                return true;
            }

            final String name = args[0];
            final String materialName = args[1];
            final SavedPosition pos = this.handler.getByName(this.type, playerSender, name);

            if (pos == null) {
                ChatUtils.sendPlayerError(playerSender, String.format("No %s with the name '%s' exists.", this.type.getDisplayName(), name));
                return true;
            }

            Material material;

            try {
                material = Material.valueOf(materialName.toUpperCase());
            } catch (final IllegalArgumentException e) {
                ChatUtils.sendPlayerError(playerSender, String.format("Item '%s' doesn't exist.", materialName));
                return true;
            }

            this.handler.delete(pos);
            pos.setBlock(material);
            this.handler.create(pos);

            ChatUtils.sendPlayerInfo(playerSender, String.format(
                    "The %s %s%s%s has been updated successfully !",
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
        if (commandSender instanceof final Player player) {
            if (args.length == 1) {
                List<SavedPosition> all = this.handler.getAllByPlayer(this.type, player);

                return all.stream()
                        .map(SavedPosition::getName)
                        .toList();
            }
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
}
