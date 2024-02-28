package fr.gamecreep.basichomes.commands.get;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.menus.DefaultMenu;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class GetCommand implements CommandExecutor {
    private final BasicHomes plugin;
    private final Permission permission;
    private final DefaultMenu menu;

    protected GetCommand(BasicHomes plugin, Permission permission, DefaultMenu menu) {
        this.plugin = plugin;
        this.permission = permission;
        this.menu = menu;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            if (!playerSender.hasPermission(this.permission.getName())) {
                this.plugin.getChatUtils().sendNoPermission(playerSender, this.permission);
                return true;
            }

            int currentPage = 1;
            this.menu.openInventory(playerSender, currentPage);

            return true;
        }

        return false;
    }
}
