package fr.gamecreep.basichomes.commands.homes;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.files.PositionDataHandler;
import fr.gamecreep.basichomes.utils.ChatUtils;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteHomeOf {
    private final Permission permission;
    private final PositionDataHandler handler;

    public DeleteHomeOf(@NonNull final BasicHomes plugin) {
        this.permission = Permission.MANAGE_HOME;
        this.handler = plugin.getPositionDataHandler();
    }

    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull String[] args) {
        if (commandSender instanceof Player playerSender) {

            if (!playerSender.hasPermission(this.permission.getName())) {
                ChatUtils.sendNoPermission(playerSender, this.permission);
                return true;
            }

            if (args.length < 2) {
                ChatUtils.sendPlayerError(playerSender, "Please add the name of the player and of the home to delete !");
                return false;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                ChatUtils.sendPlayerError(playerSender, "Player not found or isn't logged in.");
                return true;
            }

            String name = args[1];
            SavedPosition pos = this.handler.getByName(PositionType.HOME, target, name);

            if (pos == null) {
                ChatUtils.sendPlayerError(playerSender, "No home exists with that name !");
                return true;
            }

            this.handler.delete(pos);

            ChatUtils.sendPlayerInfo(playerSender, String.format(
                    "The home %s%s%s of %s%s%s has been removed !",
                    Constants.SPECIAL_COLOR,
                    name,
                    Constants.SUCCESS_COLOR,
                    Constants.SPECIAL_COLOR,
                    target.getName(),
                    Constants.SUCCESS_COLOR
            ));

            return true;
        }

        return false;
    }

    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull String[] args) {
        if (commandSender instanceof Player) {
            final List<String> list = new ArrayList<>();

            if (args.length == 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getDisplayName().contains(args[0])) {
                        list.add(player.getName());
                    }
                }
            } else if (args.length == 2) {
                for (SavedPosition pos : this.handler.getAll(PositionType.HOME)) {
                    if (pos.getName().contains(args[1])) {
                        list.add(pos.getName());
                    }
                }
            }

            return list;
        }

        return Collections.emptyList();
    }
}
