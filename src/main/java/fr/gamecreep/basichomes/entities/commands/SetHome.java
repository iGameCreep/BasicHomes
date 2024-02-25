package fr.gamecreep.basichomes.entities.commands;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.classes.PlayerHome;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetHome implements CommandExecutor {
    private final BasicHomes plugin;

    public SetHome(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {

        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            if (args.length == 0) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, "Please add a home name !");
                return true;
            }

            Location playerPos = playerSender.getLocation();
            String name = args[0];

            List<PlayerHome> playerHomeList = this.plugin.getHomeHandler().getAllByPlayer(playerSender);
            for (PlayerHome home : playerHomeList) {
                if (home.getName().equalsIgnoreCase(name)) {
                    this.plugin.getChatUtils().sendPlayerError(playerSender, "A home with this name already exists !");
                    return true;
                }
            }

            this.plugin.getHomeHandler().create(new PlayerHome(name, playerSender.getUniqueId().toString(), playerPos));
            this.plugin.getChatUtils().sendPlayerInfo(playerSender, String.format("Home %s%s%s has been created !", Constants.SPECIAL_COLOR, name, Constants.SUCCESS_COLOR));

            return true;
        }

        return false;
    }
}
