package fr.gamecreep.basichomes.commands.homes;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.menus.home.HomeMenu;
import fr.gamecreep.basichomes.menus.home.HomeMenuFactory;
import fr.gamecreep.basichomes.utils.ChatUtils;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetHomesOf {

    private final BasicHomes plugin;
    private final Permission permission;

    public GetHomesOf(BasicHomes plugin) {
        this.plugin = plugin;
        this.permission = Permission.MANAGE_HOME;
    }

    public boolean onCommand(@NonNull final CommandSender commandSender, @NonNull final String[] args) {
        if (commandSender instanceof Player playerSender) {
            if (!playerSender.hasPermission(this.permission.getName())) {
                ChatUtils.sendNoPermission(playerSender, this.permission);
                return true;
            }

            if (args.length < 1) {
                ChatUtils.sendPlayerError(playerSender, "Please add the name of the player !");
                return true;
            }

            final Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                ChatUtils.sendPlayerError(playerSender, "Player not found or isn't logged in.");
                return true;
            }

            final HomeMenuFactory factory = this.plugin.getHomeMenuFactory();
            final List<SavedPosition> homes = this.plugin.getPositionDataHandler().getAllByPlayer(PositionType.HOME, target);

            factory.openInventory(playerSender, new HomeMenu(this.plugin, playerSender, target, PositionType.HOME), homes);

            return true;
        }

        return false;
    }

    public List<String> onTabComplete(@NonNull final CommandSender commandSender, @NonNull final String[] args) {
        if (commandSender instanceof Player) {
            final List<String> list = new ArrayList<>();

            if (args.length == 1) {
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getDisplayName().contains(args[0])) {
                        list.add(player.getName());
                    }
                }
            }

            return list;
        }

        return Collections.emptyList();
    }
}
