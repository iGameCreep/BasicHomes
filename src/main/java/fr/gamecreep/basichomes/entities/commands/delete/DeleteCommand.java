package fr.gamecreep.basichomes.entities.commands.delete;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.classes.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.files.DataHandler;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DeleteCommand implements CommandExecutor, TabCompleter {
    private final BasicHomes plugin;
    private final PositionType type;
    private final DataHandler handler;

    protected DeleteCommand(BasicHomes plugin, PositionType type) {
        this.plugin = plugin;
        this.type = type;
        if (type == PositionType.HOME) this.handler = plugin.getHomeHandler();
        else this.handler = plugin.getWarpHandler();
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            if (args.length < 1) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, String.format("Please add the name of the %s to delete !", this.type.getDisplayName()));
                return false;
            }

            String name = args[0];
            SavedPosition pos = this.plugin.getHomeHandler().getByName(playerSender, name);

            if (pos == null) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, String.format("No %s exists with that name !", this.type.getDisplayName()));
                return true;
            }

            this.handler.delete(pos);

            this.plugin.getChatUtils().sendPlayerInfo(playerSender, String.format(
                    "The %s %s%s%s has been removed !",
                    this.type.getDisplayName(),
                    Constants.SPECIAL_COLOR,
                    name,
                    Constants.SUCCESS_COLOR
            ));

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            List<String> nameList = new ArrayList<>();
            List<SavedPosition> list = this.handler.getAllByPlayer(playerSender);

            for (SavedPosition pos : list) {
                nameList.add(pos.getName());
            }

            return nameList;
        }

        return Collections.emptyList();
    }
}
