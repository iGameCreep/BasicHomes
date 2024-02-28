package fr.gamecreep.basichomes.commands.get;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.menus.DefaultMenu;
import fr.gamecreep.basichomes.menus.HomeMenu;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetHomesOf implements CommandExecutor, TabCompleter {
    private final BasicHomes plugin;
    private final Permission permission;
    private final DefaultMenu menu;

    public GetHomesOf(BasicHomes plugin) {
        this.plugin = plugin;
        this.permission = Permission.MANAGE_HOME;
        this.menu = new HomeMenu(plugin);
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            if (!playerSender.hasPermission(this.permission.getName())) {
                this.plugin.getChatUtils().sendNoPermission(playerSender, this.permission);
                return true;
            }

            if (args.length < 1) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, "Please add the name of the player !");
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, "Player not found or isn't logged in.");
                return true;
            }

            int currentPage = 1;
            this.menu.openInventoryOf(playerSender, target, currentPage);

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (commandSender instanceof Player) {
            List<String> list = new ArrayList<>();

            if (args.length == 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    list.add(player.getName());
                }
            }

            return list;
        }

        return Collections.emptyList();
    }
}
