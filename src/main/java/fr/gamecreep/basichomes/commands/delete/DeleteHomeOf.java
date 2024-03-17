package fr.gamecreep.basichomes.commands.delete;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.classes.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.files.DataHandler;
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

public class DeleteHomeOf implements CommandExecutor, TabCompleter {
    private final BasicHomes plugin;
    private final Permission permission;
    private final DataHandler handler;

    public DeleteHomeOf(BasicHomes plugin) {
        this.plugin = plugin;
        this.permission = Permission.MANAGE_HOME;
        this.handler = plugin.getHomeHandler();
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            if (!playerSender.hasPermission(this.permission.getName())) {
                this.plugin.getChatUtils().sendNoPermission(playerSender, this.permission);
                return true;
            }

            if (args.length < 2) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, "Please add the name of the player and of the home to delete !");
                return false;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, "Player not found or isn't logged in.");
                return true;
            }

            String name = args[1];
            SavedPosition pos = this.handler.getByName(target, name);

            if (pos == null) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, "No home exists with that name !");
                return true;
            }

            this.handler.delete(pos);

            this.plugin.getChatUtils().sendPlayerInfo(playerSender, String.format(
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

    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (commandSender instanceof Player) {
            List<String> list = new ArrayList<>();

            if (args.length == 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    list.add(player.getName());
                }
            } else if (args.length == 2) {
                for (SavedPosition pos : this.handler.getAll()) {
                    list.add(pos.getName());
                }
            }

            return list;
        }

        return Collections.emptyList();
    }
}
